/**
 * TypeMaster - Typing Engine (typing.js)
 * Core logic for rendering paragraphs scaled by minute durations (1, 3, 5, 7, 9 min),
 * handling keyboard input, continuous typing, and submitting results to Spring Boot REST API.
 */

let currentParagraph = "";
let charIndex = 0;
let correctChars = 0;
let mistakes = 0;
let totalTyped = 0;
let isTestStarted = false;
let isTestFinished = false;

let paragraphBoxEl;
let hiddenInputEl;
let typingArenaEl;
let restartBtnEl;
let newParaBtnEl;
let durationSelectEl;
let modeSelectEl;
let languageSelectEl;
let topicSelectEl;
let languageGroupEl;
let topicGroupEl;

function initTypingEngine() {
  paragraphBoxEl = document.getElementById('paragraph-box');
  hiddenInputEl = document.getElementById('hidden-input');
  typingArenaEl = document.getElementById('typing-arena');
  restartBtnEl = document.getElementById('restart-btn');
  newParaBtnEl = document.getElementById('new-para-btn');
  durationSelectEl = document.getElementById('duration-select');
  modeSelectEl = document.getElementById('mode-select');
  languageSelectEl = document.getElementById('language-select');
  topicSelectEl = document.getElementById('topic-select');
  languageGroupEl = document.getElementById('language-group');
  topicGroupEl = document.getElementById('topic-group');

  if (!paragraphBoxEl || !hiddenInputEl) return;

  if (durationSelectEl) {
    durationSelectEl.addEventListener('change', (e) => {
      handleDurationChange(e.target.value);
    });
  }

  if (modeSelectEl) {
    modeSelectEl.addEventListener('change', (e) => {
      const val = e.target.value;
      if (val === 'CUSTOM' && typeof setCustomPracticeText === 'function') {
        const userText = prompt("Enter your custom text for typing practice:", getCustomPracticeText());
        if (userText && userText.trim().length > 0) {
          setCustomPracticeText(userText);
        }
      }
      handleModeUI();
      restartTest();
    });
  }

  if (languageSelectEl) {
    languageSelectEl.addEventListener('change', () => {
      restartTest();
    });
  }

  if (topicSelectEl) {
    topicSelectEl.addEventListener('change', () => {
      restartTest();
    });
  }

  if (typingArenaEl) {
    typingArenaEl.addEventListener('click', () => {
      if (hiddenInputEl) hiddenInputEl.focus();
    });
  }

  if (paragraphBoxEl) {
    paragraphBoxEl.addEventListener('click', () => {
      if (hiddenInputEl) hiddenInputEl.focus();
    });
  }

  hiddenInputEl.addEventListener('input', handleTypingInput);

  // Global keydown capture: Auto-focus hiddenInputEl whenever user types anywhere on page
  document.addEventListener('keydown', (e) => {
    const activeEl = document.activeElement;
    if (activeEl && (activeEl.tagName === 'INPUT' && activeEl.id !== 'hidden-input' || activeEl.tagName === 'SELECT' || activeEl.tagName === 'TEXTAREA')) {
      return;
    }

    if (e.altKey || e.ctrlKey || e.metaKey || e.key === 'Tab' || e.key === 'Escape' || e.key.startsWith('F')) {
      return;
    }

    if (hiddenInputEl) {
      if (document.activeElement !== hiddenInputEl) {
        hiddenInputEl.focus();
      }

      if (e.key === 'Backspace') {
        e.preventDefault();
        handleBackspace();
      } else if (e.key === 'Enter') {
        e.preventDefault();
        handleEnterKey();
      }
    }
  });

  if (restartBtnEl) {
    restartBtnEl.addEventListener('click', startTestExplicitly);
  }
  if (newParaBtnEl) newParaBtnEl.addEventListener('click', loadNewParagraph);

  const pauseBtnEl = document.getElementById('pause-btn');
  if (pauseBtnEl) {
    pauseBtnEl.addEventListener('click', togglePauseTest);
  }

  const focusBtnEl = document.getElementById('focus-btn');
  if (focusBtnEl) {
    focusBtnEl.addEventListener('click', toggleFocusMode);
  }

  const fullscreenBtnEl = document.getElementById('fullscreen-btn');
  if (fullscreenBtnEl) {
    fullscreenBtnEl.addEventListener('click', toggleFullscreenMode);
  }

  const difficultySelectEl = document.getElementById('difficulty-select');
  if (difficultySelectEl) {
    difficultySelectEl.addEventListener('change', () => {
      restartTest();
    });
  }

  handleModeUI();
  durationSelectEl = document.getElementById('duration-select');
  let savedDuration = null;
  try {
    savedDuration = localStorage.getItem('typeMaster_selectedDuration');
  } catch (e) {}

  if (savedDuration && durationSelectEl) {
    durationSelectEl.value = savedDuration;
  }
  if (durationSelectEl) {
    durationSelectEl.addEventListener('change', (e) => {
      handleDurationChange(e.target.value);
    });
  }
  const initialVal = (durationSelectEl && durationSelectEl.value) ? durationSelectEl.value : '1m';
  setTimerDuration(initialVal);
  initVirtualKeyboard();
  resetAnalyticsCharts();
  initChartsVisibility();
  initTimesUpModalHandlers();
  loadNewParagraph();
}

function handleModeUI() {
  const isCoding = modeSelectEl && modeSelectEl.value === 'CODING';
  if (languageGroupEl) {
    languageGroupEl.style.display = isCoding ? 'flex' : 'none';
  }
  if (topicGroupEl) {
    topicGroupEl.style.display = isCoding ? 'flex' : 'none';
  }
  if (paragraphBoxEl) {
    if (isCoding) paragraphBoxEl.classList.add('code-mode');
    else paragraphBoxEl.classList.remove('code-mode');
  }
}

async function loadNewParagraph() {
  resetTestState();
  const currentDurationMins = getTimerDurationMinutes();
  const mode = modeSelectEl ? modeSelectEl.value : 'PARAGRAPH';
  const language = languageSelectEl ? languageSelectEl.value : 'JAVA';
  const topic = topicSelectEl ? topicSelectEl.value : 'ALL';
  const difficultySelectEl = document.getElementById('difficulty-select');
  const difficulty = difficultySelectEl ? difficultySelectEl.value : 'MEDIUM';

  // 1. Render fallback snippet instantly so text is NEVER blank
  if (typeof getFallbackMaterial === 'function') {
    let newMaterial = getFallbackMaterial(currentDurationMins, mode, language, topic, difficulty);
    // Attempt 1 retry if exact same paragraph selected
    if (newMaterial === currentParagraph) {
      newMaterial = getFallbackMaterial(currentDurationMins, mode, language, topic, difficulty);
    }
    currentParagraph = newMaterial;
  } else {
    currentParagraph = "The quick brown fox jumps over the lazy dog. Practice typing every day to master your speed and accuracy.";
  }
  renderParagraph();

  // 2. Optionally fetch live API content in background
  if (typeof fetchPracticeMaterial === 'function') {
    try {
      const liveData = await fetchPracticeMaterial(currentDurationMins, mode, language, topic, difficulty);
      if (liveData && liveData.trim().length > 0 && !isTestStarted) {
        currentParagraph = liveData;
        renderParagraph();
      }
    } catch (err) {
      console.log('Async API load optional fallback:', err);
    }
  }
}

async function appendContinuousParagraph() {
  const currentDurationMins = getTimerDurationMinutes();
  const mode = modeSelectEl ? modeSelectEl.value : 'PARAGRAPH';
  const language = languageSelectEl ? languageSelectEl.value : 'JAVA';
  const topic = topicSelectEl ? topicSelectEl.value : 'ALL';
  const difficultySelectEl = document.getElementById('difficulty-select');
  const difficulty = difficultySelectEl ? difficultySelectEl.value : 'MEDIUM';

  let nextParaText = "";
  if (typeof fetchPracticeMaterial === 'function') {
    nextParaText = await fetchPracticeMaterial(currentDurationMins, mode, language, topic, difficulty);
  } else {
    nextParaText = getRandomParagraph(currentDurationMins);
  }

  const nextPara = (mode === 'CODING' ? "\n\n" : " ") + nextParaText;
  currentParagraph += nextPara;
  
  const chars = nextPara.split('');
  chars.forEach((char) => {
    const span = document.createElement('span');
    span.classList.add('char');
    span.textContent = char;
    paragraphBoxEl.appendChild(span);
  });

  const charSpans = paragraphBoxEl.querySelectorAll('.char');
  if (charIndex < charSpans.length) {
    charSpans[charIndex].classList.add('active');
  }
}

function renderParagraph() {
  if (!paragraphBoxEl) return;
  paragraphBoxEl.innerHTML = '';

  const chars = currentParagraph.split('');
  chars.forEach((char, index) => {
    const span = document.createElement('span');
    span.classList.add('char');
    if (index === 0) span.classList.add('active');
    span.textContent = char;
    paragraphBoxEl.appendChild(span);
  });
}

let sessionKeyPresses = {};
let sessionKeyErrors = {};
let sessionCharsByType = { letters: 0, numbers: 0, symbols: 0 };

function resetSessionTracker() {
  sessionKeyPresses = {};
  sessionKeyErrors = {};
  sessionCharsByType = { letters: 0, numbers: 0, symbols: 0 };
}

function recordCharTyped(typedChar, isCorrect) {
  if (!typedChar) return;
  const keyKey = typedChar.toUpperCase();
  sessionKeyPresses[keyKey] = (sessionKeyPresses[keyKey] || 0) + 1;

  if (/[a-zA-Z]/.test(typedChar)) {
    sessionCharsByType.letters++;
  } else if (/[0-9]/.test(typedChar)) {
    sessionCharsByType.numbers++;
  } else {
    sessionCharsByType.symbols++;
  }

  if (!isCorrect) {
    sessionKeyErrors[keyKey] = (sessionKeyErrors[keyKey] || 0) + 1;
  }
}

function handleTypingInput(e) {
  if (isTestFinished) return;

  const inputVal = hiddenInputEl.value;
  const typedChar = inputVal.slice(-1);

  if (!typedChar) return;

  if (!isTestStarted) {
    isTestStarted = true;
    startTimer(onTimerTick, finishTest);
    if (restartBtnEl) restartBtnEl.innerHTML = '↻ Restart Test';
    const statusBadge = document.getElementById('test-status-badge');
    if (statusBadge) {
      statusBadge.textContent = 'In Progress';
      statusBadge.className = 'badge badge-warning';
    }
  }

  const charSpans = paragraphBoxEl.querySelectorAll('.char');
  if (charIndex < charSpans.length) {
    const expectedChar = currentParagraph[charIndex];
    const currentSpan = charSpans[charIndex];

    currentSpan.classList.remove('active');
    totalTyped++;

    const isMatch = typedChar === expectedChar;
    recordCharTyped(expectedChar, isMatch);

    if (isMatch) {
      currentSpan.classList.add('correct');
      correctChars++;
      flashKeyFeedback(typedChar, true);
    } else {
      currentSpan.classList.add('incorrect');
      mistakes++;
      flashKeyFeedback(typedChar, false);
    }

    charIndex++;

    if (charIndex < charSpans.length) {
      charSpans[charIndex].classList.add('active');
    } else {
      appendContinuousParagraph();
    }
  }

  hiddenInputEl.value = '';
  updateLiveStats();
}

function handleBackspace() {
  if (isTestFinished || charIndex <= 0) return;
  flashKeyFeedback('backspace', true);

  const charSpans = paragraphBoxEl.querySelectorAll('.char');

  if (charIndex < charSpans.length) {
    charSpans[charIndex].classList.remove('active');
  }

  charIndex--;
  const prevSpan = charSpans[charIndex];

  if (prevSpan.classList.contains('correct')) {
    correctChars = Math.max(0, correctChars - 1);
  } else if (prevSpan.classList.contains('incorrect')) {
    mistakes = Math.max(0, mistakes - 1);
  }

  prevSpan.classList.remove('correct', 'incorrect');
  prevSpan.classList.add('active');

  updateLiveStats();
}

function handleEnterKey() {
  if (isTestFinished) return;

  if (!isTestStarted) {
    isTestStarted = true;
    startTimer(onTimerTick, finishTest);
  }

  const charSpans = paragraphBoxEl.querySelectorAll('.char');
  if (charIndex < charSpans.length) {
    const expectedChar = currentParagraph[charIndex];
    const currentSpan = charSpans[charIndex];

    currentSpan.classList.remove('active');
    totalTyped++;

    if (expectedChar === '\n' || expectedChar === '\r') {
      currentSpan.classList.add('correct');
      correctChars++;
    } else {
      currentSpan.classList.add('incorrect');
      mistakes++;
    }

    charIndex++;

    if (charIndex < charSpans.length) {
      charSpans[charIndex].classList.add('active');
    } else {
      appendContinuousParagraph();
    }
  }

  if (hiddenInputEl) hiddenInputEl.value = '';
  updateLiveStats();
}

function updateProgressBar() {
  const fillEl = document.getElementById('typing-progress-fill');
  const percentEl = document.getElementById('typing-progress-percent');
  if (!fillEl && !percentEl) return;

  const totalChars = (currentParagraph && currentParagraph.length) ? currentParagraph.length : 1;
  const progressPercent = Math.min(100, Math.max(0, Math.round((charIndex / totalChars) * 100)));

  if (fillEl) fillEl.style.width = `${progressPercent}%`;
  if (percentEl) percentEl.textContent = `${progressPercent}%`;
}

function updateLiveStats() {
  const timeElapsed = getTimeElapsed();
  const currentWpm = calculateWPM(correctChars, timeElapsed);
  const rawWpm = typeof calculateRawWPM === 'function' ? calculateRawWPM(totalTyped, timeElapsed) : currentWpm;
  const currentAccuracy = calculateAccuracy(correctChars, totalTyped);
  const totalLength = currentParagraph ? currentParagraph.length : 0;
  const remainingChars = Math.max(0, totalLength - charIndex);
  const consistency = typeof calculateConsistency === 'function' ? calculateConsistency(currentAccuracy, mistakes, timeElapsed) : currentAccuracy;

  updateLiveStatsUI({
    wpm: currentWpm,
    rawWpm: rawWpm,
    accuracy: currentAccuracy,
    mistakes: mistakes,
    correctChars: correctChars,
    typedChars: totalTyped,
    remainingChars: remainingChars,
    consistency: consistency,
    timeElapsed: timeElapsed
  });
  updateProgressBar();
  highlightNextKey();
}

/* ==========================================================================
   VIRTUAL KEYBOARD ENGINE & HIGHLIGHT SYSTEM
   ========================================================================== */

const SHIFT_SYMBOLS_MAP = {
  '~': '`', '!': '1', '@': '2', '#': '3', '$': '4', '%': '5', '^': '6',
  '&': '7', '*': '8', '(': '9', ')': '0', '_': '-', '+': '=',
  '{': '[', '}': ']', '|': '\\', ':': ';', '"': "'", '<': ',', '>': '.', '?': '/'
};

function clearKeyHighlights() {
  document.querySelectorAll('.key-btn').forEach(btn => {
    btn.classList.remove('key-next');
  });
}

function cssEscape(str) {
  if (!str) return '';
  return str.replace(/([\\"'`#.:?=+*^$()\[\]{}|&><])/g, '\\$1');
}

function flashKeyFeedback(char, isCorrect) {
  if (!char) return;
  const lowerChar = char.toLowerCase();
  const feedbackClass = isCorrect ? 'key-correct' : 'key-incorrect';

  let keyBtn = document.querySelector(`.key-btn[data-key="${cssEscape(lowerChar)}"]`) ||
               document.querySelector(`.key-btn[data-shift="${cssEscape(char)}"]`);

  if (!keyBtn && char === ' ') {
    keyBtn = document.querySelector('.key-btn[data-key=" "]');
  } else if (!keyBtn && (char === 'enter' || char === '\n' || char === '\r')) {
    keyBtn = document.querySelector('.key-btn[data-key="enter"]');
  } else if (!keyBtn && char === 'backspace') {
    keyBtn = document.querySelector('.key-btn[data-key="backspace"]');
  }

  if (keyBtn) {
    keyBtn.classList.add(feedbackClass);
    setTimeout(() => {
      keyBtn.classList.remove(feedbackClass);
    }, 220);
  }
}

function highlightNextKey() {
  clearKeyHighlights();
  if (isTestFinished || !currentParagraph || charIndex >= currentParagraph.length) return;

  const expectedChar = currentParagraph[charIndex];
  if (!expectedChar) return;

  let baseKey = expectedChar.toLowerCase();
  let needsShift = false;

  if (expectedChar >= 'A' && expectedChar <= 'Z') {
    needsShift = true;
  } else if (SHIFT_SYMBOLS_MAP[expectedChar]) {
    needsShift = true;
    baseKey = SHIFT_SYMBOLS_MAP[expectedChar];
  } else if (expectedChar === '\n' || expectedChar === '\r') {
    baseKey = 'enter';
  } else if (expectedChar === '\t') {
    baseKey = 'tab';
  }

  // Highlight target base key (Blue)
  let targetBtn = document.querySelector(`.key-btn[data-key="${cssEscape(baseKey)}"]`);
  if (!targetBtn && expectedChar) {
    targetBtn = document.querySelector(`.key-btn[data-shift="${cssEscape(expectedChar)}"]`);
  }

  if (targetBtn) {
    targetBtn.classList.add('key-next');
  }

  // Highlight Shift keys if Shift is required
  if (needsShift) {
    document.querySelectorAll('.key-btn[data-key="shift"]').forEach(shiftBtn => {
      shiftBtn.classList.add('key-next');
    });
  }
}

function initVirtualKeyboard() {
  const keyboardWrapper = document.getElementById('virtual-keyboard');
  if (!keyboardWrapper) return;

  keyboardWrapper.addEventListener('click', (e) => {
    const btn = e.target.closest('.key-btn');
    if (!btn) return;

    const keyVal = btn.dataset.key;
    const shiftVal = btn.dataset.shift;

    if (!hiddenInputEl) return;

    if (keyVal === 'backspace') {
      handleBackspace();
    } else if (keyVal === 'enter') {
      handleEnterKey();
    } else if (keyVal === 'shift' || keyVal === 'capslock' || keyVal === 'tab') {
      hiddenInputEl.focus();
    } else {
      let charToInsert = keyVal;
      if (shiftVal && (btn.classList.contains('key-next') || e.shiftKey)) {
        charToInsert = shiftVal;
      }
      hiddenInputEl.value = charToInsert;
      handleTypingInput();
      hiddenInputEl.focus();
    }
  });

  // Physical keyboard keydown pressed highlight
  document.addEventListener('keydown', (e) => {
    let keyName = e.key.toLowerCase();
    if (keyName === ' ') keyName = ' ';

    let keyBtn = document.querySelector(`.key-btn[data-key="${cssEscape(keyName)}"]`) ||
                 document.querySelector(`.key-btn[data-shift="${cssEscape(e.key)}"]`);

    if (keyBtn) {
      keyBtn.classList.add('key-pressed');
    }
  });

  // Physical keyboard keyup release highlight
  document.addEventListener('keyup', (e) => {
    let keyName = e.key.toLowerCase();
    if (keyName === ' ') keyName = ' ';

    let keyBtn = document.querySelector(`.key-btn[data-key="${cssEscape(keyName)}"]`) ||
                 document.querySelector(`.key-btn[data-shift="${cssEscape(e.key)}"]`);

    if (keyBtn) {
      keyBtn.classList.remove('key-pressed');
    } else {
      document.querySelectorAll('.key-pressed').forEach(btn => btn.classList.remove('key-pressed'));
    }
  });

  highlightNextKey();
}

/* ==========================================================================
   REAL-TIME CANVAS ANALYTICS GRAPH ENGINE (WPM & ACCURACY)
   ========================================================================== */

let wpmChartData = [0];
let accuracyChartData = [100];

function resetAnalyticsCharts() {
  wpmChartData = [0];
  accuracyChartData = [100];
  const wpmBadge = document.getElementById('live-wpm-badge');
  const accBadge = document.getElementById('live-accuracy-badge');
  if (wpmBadge) wpmBadge.textContent = '0 WPM';
  if (accBadge) accBadge.textContent = '100%';
  drawCanvasChart('wpm-chart', wpmChartData, '--primary-color', '#2563eb', 120);
  drawCanvasChart('accuracy-chart', accuracyChartData, '--success-color', '#22c55e', 100);
}

function updateAnalyticsCharts(wpm, accuracy) {
  wpmChartData.push(wpm);
  accuracyChartData.push(accuracy);

  const wpmBadge = document.getElementById('live-wpm-badge');
  const accBadge = document.getElementById('live-accuracy-badge');
  if (wpmBadge) wpmBadge.textContent = `${wpm} WPM`;
  if (accBadge) accBadge.textContent = `${accuracy}%`;

  const maxWpm = Math.max(100, Math.max(...wpmChartData) + 15);
  drawCanvasChart('wpm-chart', wpmChartData, '--primary-color', '#2563eb', maxWpm);
  drawCanvasChart('accuracy-chart', accuracyChartData, '--success-color', '#22c55e', 100);
}

function drawCanvasChart(canvasId, dataPoints, themeVarName, fallbackColor, maxY) {
  const canvas = document.getElementById(canvasId);
  if (!canvas) return;
  const ctx = canvas.getContext('2d');
  if (!ctx) return;

  const rect = canvas.getBoundingClientRect();
  const width = rect.width || canvas.width || 300;
  const height = rect.height || canvas.height || 130;

  const dpr = window.devicePixelRatio || 1;
  if (canvas.width !== Math.floor(width * dpr) || canvas.height !== Math.floor(height * dpr)) {
    canvas.width = Math.floor(width * dpr);
    canvas.height = Math.floor(height * dpr);
  }
  ctx.save();
  ctx.scale(dpr, dpr);
  ctx.clearRect(0, 0, width, height);

  let strokeColor = fallbackColor;
  try {
    const computed = getComputedStyle(document.documentElement).getPropertyValue(themeVarName).trim();
    if (computed) strokeColor = computed;
  } catch (e) {}

  ctx.strokeStyle = 'rgba(148, 163, 184, 0.15)';
  ctx.lineWidth = 1;
  const gridSteps = 3;
  for (let i = 1; i <= gridSteps; i++) {
    const y = (height / (gridSteps + 1)) * i;
    ctx.beginPath();
    ctx.moveTo(0, y);
    ctx.lineTo(width, y);
    ctx.stroke();
  }

  if (!dataPoints || dataPoints.length === 0) {
    ctx.restore();
    return;
  }

  const paddingX = 10;
  const paddingY = 12;
  const graphW = width - paddingX * 2;
  const graphH = height - paddingY * 2;

  const points = dataPoints.map((val, index) => {
    const x = paddingX + (index / Math.max(1, dataPoints.length - 1)) * graphW;
    const normY = Math.min(1, Math.max(0, val / maxY));
    const y = paddingY + (1 - normY) * graphH;
    return { x, y };
  });

  const gradient = ctx.createLinearGradient(0, paddingY, 0, height);
  try {
    let fillColor = fillGradientColor || 'rgba(37, 99, 235, 0.25)';
    if (!fillGradientColor && strokeColor) {
      if (strokeColor === '#10b981') fillColor = 'rgba(16, 185, 129, 0.25)';
      else if (strokeColor === '#ef4444') fillColor = 'rgba(239, 68, 68, 0.25)';
      else if (strokeColor === '#f59e0b') fillColor = 'rgba(245, 158, 11, 0.25)';
    }
    gradient.addColorStop(0, fillColor);
    gradient.addColorStop(1, 'rgba(0, 0, 0, 0)');
  } catch (e) {
    gradient.addColorStop(0, 'rgba(37, 99, 235, 0.2)');
    gradient.addColorStop(1, 'rgba(0, 0, 0, 0)');
  }

  ctx.beginPath();
  ctx.moveTo(points[0].x, points[0].y);
  for (let i = 0; i < points.length - 1; i++) {
    const xc = (points[i].x + points[i + 1].x) / 2;
    const yc = (points[i].y + points[i + 1].y) / 2;
    ctx.quadraticCurveTo(points[i].x, points[i].y, xc, yc);
  }
  if (points.length > 1) {
    ctx.lineTo(points[points.length - 1].x, points[points.length - 1].y);
  }
  ctx.lineTo(points[points.length - 1].x, height - 2);
  ctx.lineTo(points[0].x, height - 2);
  ctx.closePath();
  ctx.fillStyle = gradient;
  ctx.fill();

  ctx.beginPath();
  ctx.moveTo(points[0].x, points[0].y);
  for (let i = 0; i < points.length - 1; i++) {
    const xc = (points[i].x + points[i + 1].x) / 2;
    const yc = (points[i].y + points[i + 1].y) / 2;
    ctx.quadraticCurveTo(points[i].x, points[i].y, xc, yc);
  }
  if (points.length > 1) {
    ctx.lineTo(points[points.length - 1].x, points[points.length - 1].y);
  }
  ctx.strokeStyle = strokeColor;
  ctx.lineWidth = 2.5;
  ctx.stroke();

  const lastP = points[points.length - 1];
  ctx.beginPath();
  ctx.arc(lastP.x, lastP.y, 4.5, 0, Math.PI * 2);
  ctx.fillStyle = strokeColor;
  ctx.fill();
  ctx.lineWidth = 2;
  ctx.strokeStyle = '#ffffff';
  ctx.stroke();

  ctx.restore();
}

function onTimerTick(timeRemaining, timeElapsed) {
  updateLiveStats();
  const currentWpm = calculateWPM(correctChars, timeElapsed);
  const currentAccuracy = calculateAccuracy(correctChars, totalTyped);
  updateAnalyticsCharts(currentWpm, currentAccuracy);
}

async function finishTest() {
  if (isTestFinished) return;
  isTestFinished = true;
  stopTimer();

  const finalTimeElapsed = getTimeElapsed() || getTimerDurationSeconds();
  const finalWpm = calculateWPM(correctChars, finalTimeElapsed);
  const finalAccuracy = calculateAccuracy(correctChars, totalTyped);

  const user = getCurrentUser();
  const testResults = {
    wpm: finalWpm,
    accuracy: finalAccuracy,
    mistakes: mistakes,
    typedChars: totalTyped,
    correctChars: correctChars,
    timeTakenSeconds: finalTimeElapsed,
    durationMinutes: getTimerDurationMinutes(),
    username: user ? user.username : null
  };

  // 1. Save to local storage for immediate frontend access
  saveTestResult({
    ...testResults,
    timeTaken: finalTimeElapsed
  });
  if (typeof saveUserHistoryResult === 'function') {
    saveUserHistoryResult({
      ...testResults,
      timeTaken: finalTimeElapsed
    });
  }

  // 2. Save detailed session analytics for Heatmap & Error Analysis Dashboard
  const latestAnalytics = {
    keyPresses: sessionKeyPresses,
    keyErrors: sessionKeyErrors,
    charsByType: sessionCharsByType,
    wpm: finalWpm,
    accuracy: finalAccuracy,
    mistakes: mistakes,
    typedChars: totalTyped,
    correctChars: correctChars,
    timeTakenSeconds: finalTimeElapsed
  };
  localStorage.setItem('typeMaster_latestAnalytics', JSON.stringify(latestAnalytics));

  if (typeof updatePersonalBestRecord === 'function') {
    updatePersonalBestRecord(finalWpm, finalAccuracy);
  }

  // 2. Post test results to Spring Boot REST API
  try {
    await fetch('/api/results', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(testResults)
    });
  } catch (err) {
    console.log('Failed to post test results:', err);
  }

  if (typeof playSoundFX === 'function') playSoundFX('completion');

  showTimesUpModal();
}

let isUntimedPracticeMode = false;

function showTimesUpModal() {
  const modal = document.getElementById('timesup-modal');
  if (modal) modal.style.display = 'flex';
}

function hideTimesUpModal() {
  const modal = document.getElementById('timesup-modal');
  if (modal) modal.style.display = 'none';
}

function initTimesUpModalHandlers() {
  const resBtn = document.getElementById('timesup-results-btn');
  const contBtn = document.getElementById('timesup-continue-btn');
  const restBtn = document.getElementById('timesup-restart-btn');

  if (resBtn) {
    resBtn.addEventListener('click', () => {
      window.location.href = 'result.html';
    });
  }

  if (contBtn) {
    contBtn.addEventListener('click', () => {
      hideTimesUpModal();
      isUntimedPracticeMode = true;
      const statusBadge = document.getElementById('test-status-badge');
      if (statusBadge) {
        statusBadge.textContent = 'Untimed Practice Mode';
        statusBadge.className = 'badge badge-secondary';
      }
      if (hiddenInputEl) hiddenInputEl.focus();
    });
  }

  if (restBtn) {
    restBtn.addEventListener('click', () => {
      hideTimesUpModal();
      restartTest();
    });
  }
}

let isTestPaused = false;

function togglePauseTest() {
  const pauseBtnEl = document.getElementById('pause-btn');
  if (!isTestStarted || isTestFinished) return;

  isTestPaused = !isTestPaused;
  if (isTestPaused) {
    pauseTimer();
    if (pauseBtnEl) pauseBtnEl.innerHTML = '▶️ Resume Test';
    if (paragraphBoxEl) paragraphBoxEl.classList.add('paused');
  } else {
    resumeTimer();
    if (pauseBtnEl) pauseBtnEl.innerHTML = '⏸️ Pause Test';
    if (paragraphBoxEl) paragraphBoxEl.classList.remove('paused');
  }
}

function toggleFocusMode() {
  document.body.classList.toggle('focus-mode-active');
  const isActive = document.body.classList.contains('focus-mode-active');
  const exitBtn = document.getElementById('exit-focus-btn');
  if (exitBtn) exitBtn.style.display = isActive ? 'block' : 'none';
}

function toggleLiveCharts() {
  const panel = document.getElementById('charts-panel');
  const btnText = document.getElementById('charts-toggle-text');
  const btnIcon = document.getElementById('charts-toggle-icon');
  if (!panel) return;

  const isHidden = panel.style.display === 'none' || getComputedStyle(panel).display === 'none';
  if (isHidden) {
    panel.style.display = 'grid';
    if (btnText) btnText.textContent = 'Hide Live Performance Graphs';
    if (btnIcon) btnIcon.textContent = '🔽';
    localStorage.setItem('typeMaster_showCharts', 'true');
    resetAnalyticsCharts();
  } else {
    panel.style.display = 'none';
    if (btnText) btnText.textContent = 'Show Live Performance Graphs';
    if (btnIcon) btnIcon.textContent = '📊';
    localStorage.setItem('typeMaster_showCharts', 'false');
  }
}

function initChartsVisibility() {
  const showCharts = localStorage.getItem('typeMaster_showCharts');
  const panel = document.getElementById('charts-panel');
  const btnText = document.getElementById('charts-toggle-text');
  const btnIcon = document.getElementById('charts-toggle-icon');

  if (showCharts === 'true' && panel) {
    panel.style.display = 'grid';
    if (btnText) btnText.textContent = 'Hide Live Performance Graphs';
    if (btnIcon) btnIcon.textContent = '🔽';
  } else if (panel) {
    panel.style.display = 'none';
    if (btnText) btnText.textContent = 'Show Live Performance Graphs';
    if (btnIcon) btnIcon.textContent = '📊';
  }
}

function toggleFullscreenMode() {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen().catch(err => console.log(err));
  } else {
    if (document.exitFullscreen) document.exitFullscreen();
  }
}

function restartTest() {
  TimerManager.reset();
  charIndex = 0;
  correctChars = 0;
  mistakes = 0;
  totalTyped = 0;
  isTestStarted = false;
  isTestFinished = false;
  isUntimedPracticeMode = false;

  if (hiddenInputEl) hiddenInputEl.value = '';

  loadNewParagraph();
}

function resetTestState() {
  TimerManager.reset();
  charIndex = 0;
  correctChars = 0;
  mistakes = 0;
  totalTyped = 0;
  isTestStarted = false;
  isTestFinished = false;
  isUntimedPracticeMode = false;

  if (hiddenInputEl) hiddenInputEl.value = '';

  updateLiveStatsUI({
    wpm: 0,
    accuracy: 100,
    mistakes: 0,
    typedChars: 0
  });
  updateProgressBar();
  try {
    resetAnalyticsCharts();
  } catch (e) {
    console.log('Analytics chart reset ignored:', e);
  }

  if (restartBtnEl) restartBtnEl.innerHTML = '▶ Start Test';
  const statusBadge = document.getElementById('test-status-badge');
  if (statusBadge) {
    statusBadge.textContent = 'Ready';
    statusBadge.className = 'badge badge-primary';
  }

  if (hiddenInputEl) hiddenInputEl.focus();
}

function startTestExplicitly() {
  if (isTestStarted && !isTestFinished) {
    restartTest();
    return;
  }

  restartTest();

  if (!isTestStarted) {
    isTestStarted = true;
    startTimer(onTimerTick, finishTest);
    if (restartBtnEl) restartBtnEl.innerHTML = '↻ Restart Test';
    const statusBadge = document.getElementById('test-status-badge');
    if (statusBadge) {
      statusBadge.textContent = 'In Progress';
      statusBadge.className = 'badge badge-warning';
    }
  }

  if (hiddenInputEl) hiddenInputEl.focus();
}

function handleDurationChange(val) {
  const durationSelectEl = document.getElementById('duration-select');
  const selectedVal = val || (durationSelectEl ? durationSelectEl.value : '1m');

  if (selectedVal === 'CUSTOM') {
    const customSecs = prompt("Enter custom test duration in seconds (10 to 600 seconds):", "45");
    if (customSecs) {
      const parsed = parseInt(customSecs, 10);
      if (!isNaN(parsed) && parsed >= 5) {
        TimerManager.setDuration('CUSTOM', parsed);
      } else {
        TimerManager.setDuration('CUSTOM', 45);
      }
    } else {
      TimerManager.setDuration('1m');
      if (durationSelectEl) durationSelectEl.value = '1m';
    }
  } else {
    TimerManager.setDuration(selectedVal);
    if (durationSelectEl) durationSelectEl.value = selectedVal;
  }

  // Force immediate update of #stat-timer DOM element
  const timerEl = document.getElementById('stat-timer');
  if (timerEl) {
    timerEl.textContent = TimerManager.formatTime(TimerManager.getMaxSeconds());
  }

  restartTest();
}
window.handleDurationChange = handleDurationChange;
window.restartTest = restartTest;
window.handleModeUI = handleModeUI;

document.addEventListener('DOMContentLoaded', () => {
  if (document.getElementById('paragraph-box') || window.location.pathname.includes('typing')) {
    initTypingEngine();
  }
});
