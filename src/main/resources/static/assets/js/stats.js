/**
 * TypeMaster - Statistics Calculation Engine (stats.js)
 * Calculates WPM, Accuracy %, Mistakes, Characters typed, and evaluates performance ratings.
 */

function calculateWPM(correctChars, timeElapsedSeconds) {
  if (!timeElapsedSeconds || timeElapsedSeconds <= 0 || !correctChars || correctChars <= 0) {
    return 0;
  }
  const timeInMinutes = timeElapsedSeconds / 60;
  const words = correctChars / 5;
  const wpm = Math.round(words / timeInMinutes);
  return isNaN(wpm) || !isFinite(wpm) ? 0 : wpm;
}

function calculateAccuracy(correctChars, totalTyped) {
  if (!totalTyped || totalTyped <= 0) {
    return 100;
  }
  const accuracy = Math.round((correctChars / totalTyped) * 100);
  return Math.max(0, Math.min(100, accuracy));
}

function getPerformanceFeedback(wpm, accuracy) {
  if (wpm >= 70 && accuracy >= 95) {
    return {
      title: "Speed Demon! 🚀",
      message: "Outstanding performance! You are typing at an elite professional level with incredible precision.",
      badgeIcon: "⚡",
      badgeClass: "elite"
    };
  } else if (wpm >= 50 && accuracy >= 90) {
    return {
      title: "Master Typist! 🌟",
      message: "Fantastic job! Your typing speed is well above average and your accuracy is impressive.",
      badgeIcon: "🏆",
      badgeClass: "pro"
    };
  } else if (wpm >= 30 && accuracy >= 80) {
    return {
      title: "Great Effort! 💪",
      message: "Good job! You have solid speed and accuracy. Consistent daily practice will push you higher.",
      badgeIcon: "🎯",
      badgeClass: "good"
    };
  } else {
    return {
      title: "Keep Practicing! 📈",
      message: "Every expert was once a beginner. Focus on accuracy first, and speed will follow naturally!",
      badgeIcon: "🌱",
      badgeClass: "beginner"
    };
  }
}

function calculateRawWPM(totalTyped, timeElapsedSeconds) {
  if (!timeElapsedSeconds || timeElapsedSeconds <= 0 || !totalTyped || totalTyped <= 0) {
    return 0;
  }
  const timeInMinutes = timeElapsedSeconds / 60;
  const rawWords = totalTyped / 5;
  const rawWpm = Math.round(rawWords / timeInMinutes);
  return isNaN(rawWpm) || !isFinite(rawWpm) ? 0 : rawWpm;
}

function calculateConsistency(accuracy, mistakes, timeElapsedSeconds) {
  if (!timeElapsedSeconds || timeElapsedSeconds <= 0) return 100;
  const penalty = Math.min(45, Math.round((mistakes / Math.max(1, timeElapsedSeconds)) * 18));
  const consistency = Math.max(50, Math.min(100, Math.round(accuracy - penalty)));
  return isNaN(consistency) ? 100 : consistency;
}

function getTypingRank(wpm) {
  if (wpm >= 110) return { name: "Master", badge: "🚀 Master" };
  if (wpm >= 90) return { name: "Diamond", badge: "👑 Diamond" };
  if (wpm >= 70) return { name: "Platinum", badge: "💎 Platinum" };
  if (wpm >= 50) return { name: "Gold", badge: "🥇 Gold" };
  if (wpm >= 30) return { name: "Silver", badge: "🥈 Silver" };
  return { name: "Bronze", badge: "🥉 Bronze" };
}

const _statsDomCache = {};

function _getStatElement(id) {
  if (!_statsDomCache[id]) {
    _statsDomCache[id] = document.getElementById(id);
  }
  return _statsDomCache[id];
}

function _setTextIfChanged(el, val) {
  if (el) {
    const strVal = String(val);
    if (el.textContent !== strVal) {
      el.textContent = strVal;
    }
  }
}

function updateLiveStatsUI(stats) {
  const wpmEl = _getStatElement('stat-wpm');
  const rawWpmEl = _getStatElement('stat-raw-wpm');
  const accuracyEl = _getStatElement('stat-accuracy');
  const mistakesEl = _getStatElement('stat-mistakes');
  const correctEl = _getStatElement('stat-correct');
  const typedEl = _getStatElement('stat-typed');
  const wordsEl = _getStatElement('stat-words');
  const remainingEl = _getStatElement('stat-remaining');
  const consistencyEl = _getStatElement('stat-consistency');
  const rankEl = _getStatElement('stat-rank');

  const netWpm = stats.wpm ?? 0;
  const rawWpm = stats.rawWpm ?? netWpm;
  const accuracy = stats.accuracy ?? 100;
  const mistakes = stats.mistakes ?? 0;
  const correctChars = stats.correctChars ?? Math.max(0, (stats.typedChars ?? 0) - mistakes);
  const typedChars = stats.typedChars ?? 0;
  const wordsTyped = Math.floor(typedChars / 5);
  const remainingChars = stats.remainingChars ?? 0;
  const consistency = stats.consistency ?? calculateConsistency(accuracy, mistakes, stats.timeElapsed);
  const rankObj = getTypingRank(netWpm);

  _setTextIfChanged(wpmEl, netWpm);
  _setTextIfChanged(rawWpmEl, rawWpm);
  _setTextIfChanged(accuracyEl, `${accuracy}%`);
  _setTextIfChanged(mistakesEl, mistakes);
  _setTextIfChanged(correctEl, correctChars);
  _setTextIfChanged(typedEl, typedChars);
  _setTextIfChanged(wordsEl, wordsTyped);
  _setTextIfChanged(remainingEl, remainingChars);
  _setTextIfChanged(consistencyEl, `${consistency}%`);
  _setTextIfChanged(rankEl, rankObj.badge);
}
