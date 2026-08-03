/**
 * TypeMaster - Centralized Timer Manager & Test Configuration Engine (timer.js)
 * Single Source of Truth for test duration, countdown timer, time tracking, and duration schema.
 */

const TimerManager = {
  // Centralized Configuration State
  config: {
    durationKey: '1m',       // Selected option value ('15s', '30s', '1m', '2m', '3m', '5m', '10m', 'CUSTOM')
    maxSeconds: 60,          // Total test duration in seconds
    customSeconds: 45        // Custom user-defined seconds
  },

  // Dynamic Runtime State
  state: {
    timeRemaining: 60,
    timeElapsed: 0,
    isRunning: false,
    intervalId: null
  },

  /**
   * Universal Helper: Parses any duration format ('15s', '30s', '1m', '2m', '3m', '5m', '10m', '3 Minutes', numbers) into total seconds.
   */
  parseDurationToSeconds(val) {
    if (val === null || val === undefined) return 60;
    if (typeof val === 'number') {
      return val <= 10 ? val * 60 : Math.max(5, val);
    }

    const str = String(val).trim().toLowerCase();

    // Check for explicit seconds: '15s', '30s', '15 sec', '30 seconds'
    if (str.includes('sec') || (str.endsWith('s') && !str.includes('min'))) {
      const numMatch = str.match(/\d+/);
      if (numMatch) {
        const secs = parseInt(numMatch[0], 10);
        if (!isNaN(secs)) return Math.max(5, secs);
      }
    }

    // Check for minutes: '1m', '2m', '3m', '5m', '10m', '1 min', '3 minutes', '5 mins'
    if (str.includes('m') || str.includes('min')) {
      const numMatch = str.match(/\d+/);
      if (numMatch) {
        const mins = parseInt(numMatch[0], 10);
        if (!isNaN(mins)) return mins * 60;
      }
    }

    // Fallback parsing for raw numbers (e.g. "3", "5", "180")
    const numMatch = str.match(/\d+/);
    if (numMatch) {
      const num = parseInt(numMatch[0], 10);
      if (!isNaN(num)) {
        return num <= 10 ? num * 60 : Math.max(5, num);
      }
    }

    return 60;
  },

  /**
   * Configures a new duration value and resets the timer UI immediately.
   */
  setDuration(durationKey, customSecs = null) {
    if (durationKey === 'CUSTOM') {
      this.config.durationKey = 'CUSTOM';
      if (customSecs) {
        this.config.customSeconds = Math.max(5, parseInt(customSecs, 10) || 45);
      }
      this.config.maxSeconds = this.config.customSeconds;
    } else {
      this.config.durationKey = String(durationKey);
      this.config.maxSeconds = this.parseDurationToSeconds(durationKey);
    }

    try {
      localStorage.setItem('typeMaster_selectedDuration', this.config.durationKey);
    } catch(e) {}

    const selectEl = document.getElementById('duration-select');
    if (selectEl && selectEl.value !== this.config.durationKey) {
      selectEl.value = this.config.durationKey;
    }

    this.state.timeRemaining = this.config.maxSeconds;
    this.state.timeElapsed = 0;
    this.updateUI();
  },

  /**
   * Returns current duration in minutes (decimal format for WPM calculation).
   */
  getDurationMinutes() {
    return this.config.maxSeconds / 60;
  },

  /**
   * Returns maximum duration in seconds.
   */
  getMaxSeconds() {
    return this.config.maxSeconds;
  },

  /**
   * Returns elapsed time in seconds.
   */
  getTimeElapsed() {
    return this.state.timeElapsed;
  },

  /**
   * Returns remaining time in seconds.
   */
  getTimeRemaining() {
    return this.state.timeRemaining;
  },

  /**
   * Resets timer state to current configured duration.
   */
  reset() {
    this.stop();

    const selectEl = document.getElementById('duration-select');
    if (selectEl) {
      if (selectEl.value && selectEl.value !== this.config.durationKey) {
        if (selectEl.value === 'CUSTOM') {
          this.config.durationKey = 'CUSTOM';
          this.config.maxSeconds = this.config.customSeconds || 45;
        } else {
          this.config.durationKey = String(selectEl.value);
          this.config.maxSeconds = this.parseDurationToSeconds(selectEl.value);
        }
      } else {
        selectEl.value = this.config.durationKey;
      }
    }

    this.state.timeRemaining = this.config.maxSeconds;
    this.state.timeElapsed = 0;
    this.updateUI();
  },

  /**
   * Starts countdown timer with tick & completion callbacks.
   */
  start(onTick, onEnd) {
    this.stop();
    this.state.isRunning = true;
    this.lockDurationDropdown(true);

    if (this.state.timeRemaining <= 0) {
      this.state.timeRemaining = this.config.maxSeconds;
      this.state.timeElapsed = 0;
    }

    this.updateUI();

    this.state.intervalId = setInterval(() => {
      if (this.state.timeRemaining <= 0) {
        this.state.timeRemaining = 0;
        this.updateUI();
        this.stop();
        if (typeof onEnd === 'function') onEnd();
        return;
      }

      this.state.timeRemaining--;
      this.state.timeElapsed++;
      this.updateUI();

      if (typeof onTick === 'function') {
        onTick(this.state.timeRemaining, this.state.timeElapsed);
      }

      if (this.state.timeRemaining <= 0) {
        this.state.timeRemaining = 0;
        this.updateUI();
        this.stop();
        if (typeof onEnd === 'function') onEnd();
      }
    }, 1000);
  },

  /**
   * Stops timer countdown.
   */
  stop() {
    if (this.state.intervalId) {
      clearInterval(this.state.intervalId);
      this.state.intervalId = null;
    }
    this.state.isRunning = false;
    this.lockDurationDropdown(false);
  },

  /**
   * Locks or unlocks the duration dropdown during active typing tests.
   */
  lockDurationDropdown(isLocked) {
    const selectEl = document.getElementById('duration-select');
    if (selectEl) {
      selectEl.disabled = isLocked;
      selectEl.title = isLocked ? "Test in progress. Restart test to change duration." : "";
      if (isLocked) {
        selectEl.classList.add('control-locked');
      } else {
        selectEl.classList.remove('control-locked');
      }
    }
  },

  /**
   * Formats total seconds into MM:SS string.
   */
  formatTime(totalSeconds) {
    const safeSecs = Math.max(0, totalSeconds);
    const mins = Math.floor(safeSecs / 60);
    const secs = safeSecs % 60;
    return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
  },

  _timerEl: null,
  _timerCardEl: null,

  _getElements() {
    if (!this._timerEl) this._timerEl = document.getElementById('stat-timer');
    if (!this._timerCardEl) this._timerCardEl = document.querySelector('.timer-card');
    return { timerEl: this._timerEl, timerCardEl: this._timerCardEl };
  },

  /**
   * Updates Timer Card display in UI.
   */
  updateUI() {
    const { timerEl, timerCardEl } = this._getElements();

    if (timerEl) {
      const formatted = this.formatTime(this.state.timeRemaining);
      if (timerEl.textContent !== formatted) {
        timerEl.textContent = formatted;
      }
    }

    if (timerCardEl && this.config.maxSeconds > 0) {
      const ratio = this.state.timeRemaining / this.config.maxSeconds;
      const targetClass = ratio > 0.5 ? 'timer-normal' : (ratio > 0.2 ? 'timer-warning' : 'timer-urgent');
      if (!timerCardEl.classList.contains(targetClass)) {
        timerCardEl.classList.remove('timer-normal', 'timer-warning', 'timer-urgent');
        timerCardEl.classList.add(targetClass);
      }
    }
  }
};

/* ==========================================================================
   BACKWARD COMPATIBILITY FUNCTION WRAPPERS
   ========================================================================== */
function setTimerDuration(durationVal) {
  TimerManager.setDuration(durationVal);
}

function getTimerDurationMinutes() {
  return TimerManager.getDurationMinutes();
}

function getTimerDurationSeconds() {
  return TimerManager.getMaxSeconds();
}

function getTimeElapsed() {
  return TimerManager.getTimeElapsed();
}

function formatTime(totalSeconds) {
  return TimerManager.formatTime(totalSeconds);
}

function updateTimerUI() {
  TimerManager.updateUI();
}

function startTimer(onTick, onEnd) {
  TimerManager.start(onTick, onEnd);
}

function stopTimer() {
  TimerManager.stop();
}

function resetTimer() {
  TimerManager.reset();
}

function pauseTimer() {
  if (TimerManager.state.intervalId) {
    clearInterval(TimerManager.state.intervalId);
    TimerManager.state.intervalId = null;
  }
  TimerManager.state.isRunning = false;
}

function resumeTimer() {
  if (TimerManager.state.isRunning) return; // already running
  // Resume from current timeRemaining without resetting elapsed time
  TimerManager.state.isRunning = true;
  TimerManager.lockDurationDropdown(true);

  TimerManager.state.intervalId = setInterval(() => {
    if (TimerManager.state.timeRemaining <= 0) {
      TimerManager.state.timeRemaining = 0;
      TimerManager.updateUI();
      TimerManager.stop();
      if (typeof finishTest === 'function') finishTest();
      return;
    }

    TimerManager.state.timeRemaining--;
    TimerManager.state.timeElapsed++;
    TimerManager.updateUI();

    if (typeof onTimerTick === 'function') {
      onTimerTick(TimerManager.state.timeRemaining, TimerManager.state.timeElapsed);
    }

    if (TimerManager.state.timeRemaining <= 0) {
      TimerManager.state.timeRemaining = 0;
      TimerManager.updateUI();
      TimerManager.stop();
      if (typeof finishTest === 'function') finishTest();
    }
  }, 1000);
}
