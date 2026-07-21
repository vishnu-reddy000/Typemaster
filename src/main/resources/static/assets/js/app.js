/**
 * TypeMaster - Common Functions & App Setup (app.js)
 * Handles navbar navigation, session state, route protection, theme system, and localStorage helpers.
 */

// Global Storage Keys
const STORAGE_KEY_LAST_RESULT = 'typeMaster_lastResult';
const STORAGE_KEY_USER = 'typeMaster_user';
const STORAGE_KEY_THEME = 'typeMaster_theme';
const STORAGE_KEY_HISTORY = 'typeMaster_userHistory';

const THEMES = [
  { id: 'light', name: 'Light', icon: '☀️' },
  { id: 'dark', name: 'Dark', icon: '🌙' },
  { id: 'glassmorphism', name: 'Glassmorphism', icon: '✨' },
  { id: 'emerald-pro', name: 'Emerald Pro', icon: '💎' },
  { id: 'focus-mode', name: 'Focus Mode', icon: '🧘' },
  { id: 'ocean-blue', name: 'Ocean Blue', icon: '🌊' },
  { id: 'sunset', name: 'Sunset Rose', icon: '🌅' }
];

/**
 * Saves completed test result to personal history array in localStorage.
 */
function saveUserHistoryResult(result) {
  try {
    const existing = getUserHistory();
    const nowIso = new Date().toISOString();
    const newEntry = {
      ...result,
      id: 'TM-' + Date.now(),
      date: nowIso,
      createdAt: nowIso
    };
    existing.unshift(newEntry);
    const trimmed = existing.slice(0, 50);
    localStorage.setItem(STORAGE_KEY_HISTORY, JSON.stringify(trimmed));
  } catch (e) {
    console.error('Failed to save user history to localStorage:', e);
  }
}

/**
 * Gets personal test history array from localStorage.
 */
function getUserHistory(username) {
  try {
    const data = localStorage.getItem(STORAGE_KEY_HISTORY);
    const list = data ? JSON.parse(data) : [];
    if (username) {
      return list.filter(item => !item.username || item.username.toLowerCase() === username.toLowerCase());
    }
    return list;
  } catch (e) {
    return [];
  }
}

/**
 * Gets currently active theme from localStorage or defaults to 'light'.
 */
function getActiveTheme() {
  try {
    return localStorage.getItem(STORAGE_KEY_THEME) || 'light';
  } catch (e) {
    return 'light';
  }
}

/**
 * Sets active theme on <html> attribute, localStorage, and syncs UI selectors.
 */
function setTheme(themeId) {
  if (!THEMES.some(t => t.id === themeId)) {
    themeId = 'light';
  }
  document.documentElement.setAttribute('data-theme', themeId);
  try {
    localStorage.setItem(STORAGE_KEY_THEME, themeId);
  } catch (e) {
    console.error('Failed to save theme to localStorage:', e);
  }

  // Sync navbar theme select dropdown if it exists
  const dropdown = document.getElementById('theme-select-dropdown');
  if (dropdown && dropdown.value !== themeId) {
    dropdown.value = themeId;
  }

  // Sync theme showcase cards if present on home page
  document.querySelectorAll('.theme-card').forEach(card => {
    if (card.dataset.theme === themeId) {
      card.classList.add('active');
    } else {
      card.classList.remove('active');
    }
  });
}

// Early execution to prevent flash of wrong theme
(function applyInitialTheme() {
  const currentTheme = getActiveTheme();
  document.documentElement.setAttribute('data-theme', currentTheme);
})();

/**
 * Utility helper to select a single DOM element.
 */
const $ = (selector) => document.querySelector(selector);

/**
 * Utility helper to select multiple DOM elements as an Array.
 */
const $$ = (selector) => Array.from(document.querySelectorAll(selector));

/**
 * Returns the currently logged in user object, or null.
 */
function getCurrentUser() {
  try {
    const data = localStorage.getItem(STORAGE_KEY_USER);
    return data ? JSON.parse(data) : null;
  } catch (e) {
    console.error('Failed to read user from localStorage:', e);
    return null;
  }
}

function saveJwtToken(token) {
  if (token) {
    localStorage.setItem('typeMaster_jwtToken', token);
  }
}

function getJwtToken() {
  return localStorage.getItem('typeMaster_jwtToken');
}

function removeJwtToken() {
  localStorage.removeItem('typeMaster_jwtToken');
}

/**
 * Helper to execute authenticated fetch calls with Bearer JWT header.
 */
async function fetchWithJwt(url, options = {}) {
  const token = getJwtToken();
  const headers = options.headers || {};
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  options.headers = headers;
  return fetch(url, options);
}

/**
 * Logs out the current user and redirects to home page.
 */
function logoutUser() {
  localStorage.removeItem(STORAGE_KEY_USER);
  removeJwtToken();
  window.location.href = 'index.html';
}

/**
 * Enforces route protection for pages requiring authentication.
 */
function enforceAuthGuard() {
  const currentPath = window.location.pathname.split('/').pop() || 'index.html';
  const protectedPages = ['typing.html', 'typing', 'result.html', 'result'];

  const user = getCurrentUser();

  if (protectedPages.includes(currentPath) && !user) {
    window.location.href = `auth.html?notice=1&redirect=${encodeURIComponent(currentPath)}`;
  }
}

/**
 * Initializes global navigation listeners (mobile toggle, active link highlight, user profile controls).
 */
function initNavigation() {
  const mobileToggle = $('.mobile-toggle');
  const navLinks = $('.nav-links');

  if (mobileToggle && navLinks) {
    mobileToggle.addEventListener('click', () => {
      navLinks.classList.toggle('show');
    });
  }

  // Highlight current page nav link based on window.location.pathname
  const currentPath = window.location.pathname.split('/').pop() || 'index.html';
  $$('.nav-links a').forEach(link => {
    const href = link.getAttribute('href');
    if (href === currentPath || (currentPath === '' && href === 'index.html')) {
      link.classList.add('active');
    } else {
      link.classList.remove('active');
    }
  });

  // Render Auth buttons & Theme dropdown in header
  renderAuthNavbar();
  renderThemeNavbarSelector();
  renderMobileNavControls();
}

/**
 * Dynamically inserts Auth controls (Sign In/Sign Up or Profile/Logout) into the navbar.
 */
function renderAuthNavbar() {
  let authContainer = $('#auth-nav-container');
  const navbarContainer = $('.navbar .container');

  if (!navbarContainer) return;

  if (!authContainer) {
    authContainer = document.createElement('div');
    authContainer.id = 'auth-nav-container';
    authContainer.className = 'auth-nav-container';
    const mobileToggle = $('.mobile-toggle');
    if (mobileToggle) {
      navbarContainer.insertBefore(authContainer, mobileToggle);
    } else {
      navbarContainer.appendChild(authContainer);
    }
  }

  const user = getCurrentUser();

  if (user && user.username) {
    authContainer.innerHTML = `
      <div class="user-badge" style="display: flex; align-items: center; gap: 0.75rem;">
        <span class="user-name-text" style="font-weight: 600; font-size: 0.9rem; color: var(--primary-color);">👤 ${escapeHtml(user.username)}</span>
        <button onclick="logoutUser()" class="btn btn-outline" style="padding: 0.35rem 0.75rem; font-size: 0.82rem;">Logout</button>
      </div>
    `;
  } else {
    authContainer.innerHTML = `
      <div class="auth-buttons" style="display: flex; align-items: center; gap: 0.5rem;">
        <a href="auth.html" class="btn btn-outline" style="padding: 0.4rem 0.85rem; font-size: 0.85rem;">Sign In</a>
        <a href="auth.html?mode=signup" class="btn btn-primary" style="padding: 0.4rem 0.85rem; font-size: 0.85rem;">Sign Up</a>
      </div>
    `;
  }
}

/**
 * Dynamically inserts Theme Selector into header navbar.
 */
function renderThemeNavbarSelector() {
  const navbarContainer = $('.navbar .container');
  if (!navbarContainer) return;

  let themeContainer = $('#theme-nav-container');
  if (!themeContainer) {
    themeContainer = document.createElement('div');
    themeContainer.id = 'theme-nav-container';
    themeContainer.className = 'theme-selector-container';

    const currentTheme = getActiveTheme();
    themeContainer.innerHTML = `
      <select id="theme-select-dropdown" class="theme-dropdown" aria-label="Select App Theme">
        ${THEMES.map(t => `<option value="${t.id}" ${t.id === currentTheme ? 'selected' : ''}>${t.icon} ${t.name}</option>`).join('')}
      </select>
    `;

    const authContainer = $('#auth-nav-container');
    const mobileToggle = $('.mobile-toggle');
    if (authContainer) {
      navbarContainer.insertBefore(themeContainer, authContainer);
    } else if (mobileToggle) {
      navbarContainer.insertBefore(themeContainer, mobileToggle);
    } else {
      navbarContainer.appendChild(themeContainer);
    }

    const dropdown = $('#theme-select-dropdown');
    if (dropdown) {
      dropdown.addEventListener('change', (e) => {
        setTheme(e.target.value);
      });
    }
  }
}

/**
 * Dynamically inserts Mobile Nav Controls (Theme selector & Auth badge) inside mobile drawer menu (.nav-links).
 */
function renderMobileNavControls() {
  const navLinks = $('.nav-links');
  if (!navLinks) return;

  let extraItem = $('#mobile-nav-extra');
  if (!extraItem) {
    extraItem = document.createElement('li');
    extraItem.id = 'mobile-nav-extra';
    extraItem.className = 'mobile-nav-extra';
    navLinks.appendChild(extraItem);
  }

  const currentTheme = getActiveTheme();
  const user = getCurrentUser();

  const themeOptionsHtml = THEMES.map(t => `<option value="${t.id}" ${t.id === currentTheme ? 'selected' : ''}>${t.icon} ${t.name}</option>`).join('');

  const authHtml = (user && user.username) ? `
    <div class="user-badge" style="display: flex; align-items: center; justify-content: space-between; width: 100%; gap: 0.75rem;">
      <span class="user-name-text" style="font-weight: 600; font-size: 0.95rem; color: var(--primary-color);">👤 ${escapeHtml(user.username)}</span>
      <button onclick="logoutUser()" class="btn btn-outline" style="padding: 0.4rem 0.9rem; font-size: 0.85rem;">Logout</button>
    </div>
  ` : `
    <div class="auth-buttons" style="display: flex; align-items: center; gap: 0.75rem; width: 100%;">
      <a href="auth.html" class="btn btn-outline" style="flex: 1; text-align: center; padding: 0.5rem; font-size: 0.85rem;">Sign In</a>
      <a href="auth.html?mode=signup" class="btn btn-primary" style="flex: 1; text-align: center; padding: 0.5rem; font-size: 0.85rem;">Sign Up</a>
    </div>
  `;

  extraItem.innerHTML = `
    <div class="theme-selector-container" style="width: 100%;">
      <select class="theme-dropdown mobile-theme-dropdown" aria-label="Select App Theme" style="width: 100%;">
        ${themeOptionsHtml}
      </select>
    </div>
    <div class="auth-nav-container" style="width: 100%;">
      ${authHtml}
    </div>
  `;

  const mobDropdown = extraItem.querySelector('.mobile-theme-dropdown');
  if (mobDropdown) {
    mobDropdown.addEventListener('change', (e) => {
      setTheme(e.target.value);
      const deskDropdown = $('#theme-select-dropdown');
      if (deskDropdown) deskDropdown.value = e.target.value;
    });
  }
}

/**
 * Initializes interactive Theme Showcase cards on Home Page.
 */
function initThemeShowcase() {
  const themeCards = $$('.theme-card');
  if (!themeCards || themeCards.length === 0) return;

  const currentTheme = getActiveTheme();

  themeCards.forEach(card => {
    const themeId = card.dataset.theme;
    if (themeId === currentTheme) {
      card.classList.add('active');
    } else {
      card.classList.remove('active');
    }

    card.addEventListener('click', () => {
      setTheme(themeId);
    });
  });
}

/**
 * Escapes HTML characters for safety.
 */
function escapeHtml(str) {
  if (!str) return '';
  return str.replace(/[&<>"']/g, function(m) {
    return {
      '&': '&amp;',
      '<': '&lt;',
      '>': '&gt;',
      '"': '&quot;',
      "'": '&#039;'
    }[m];
  });
}

/**
 * Saves test result object to localStorage.
 */
function saveTestResult(resultData) {
  try {
    localStorage.setItem(STORAGE_KEY_LAST_RESULT, JSON.stringify(resultData));
  } catch (e) {
    console.error('Failed to save result to localStorage:', e);
  }
}

/**
 * Retrieves the latest test result object from localStorage.
 */
function getTestResult() {
  try {
    const data = localStorage.getItem(STORAGE_KEY_LAST_RESULT);
    return data ? JSON.parse(data) : null;
  } catch (e) {
    console.error('Failed to read result from localStorage:', e);
    return null;
  }
}

/**
 * Handles the initial full-screen typewriter splash loading screen.
 */
function initSplashScreen() {
  const splashLoader = document.getElementById('splash-loader');
  const typewriterTextEl = document.getElementById('typewriter-text');
  const progressBarEl = document.getElementById('splash-progress-bar');
  const percentageEl = document.getElementById('splash-percentage');

  if (!splashLoader || !typewriterTextEl) return;

  const currentPath = window.location.pathname.toLowerCase();
  let fullQuote = "Ideas mean nothing without execution.";

  if (currentPath.includes('typing')) {
    fullQuote = "Work on your typing skills";
  } else if (currentPath.includes('result')) {
    fullQuote = "Analyzing your typing performance... Please wait a moment.";
  }

  let charIndex = 0;
  typewriterTextEl.textContent = "";

  // Typewriter effect synchronized with progress bar
  const typingInterval = setInterval(() => {
    if (charIndex < fullQuote.length) {
      typewriterTextEl.textContent += fullQuote.charAt(charIndex);
      charIndex++;

      const progress = Math.round((charIndex / fullQuote.length) * 100);
      if (progressBarEl) progressBarEl.style.width = `${progress}%`;
      if (percentageEl) percentageEl.textContent = `${progress}%`;
    } else {
      clearInterval(typingInterval);
      if (progressBarEl) progressBarEl.style.width = '100%';
      if (percentageEl) percentageEl.textContent = '100%';

      // Pause briefly so user can read complete quote & see background image clearly
      setTimeout(() => {
        splashLoader.classList.add('fade-out');
        setTimeout(() => {
          splashLoader.style.display = 'none';
        }, 850);
      }, 800);
    }
  }, 70);
}

/**
 * Personal Best Records Persistence & Calculation Engine
 */
function getPersonalBestRecord() {
  try {
    const raw = localStorage.getItem('typeMaster_personalBest');
    if (raw) return JSON.parse(raw);
  } catch (e) {}
  return {
    bestWpm: 0,
    bestAccuracy: 0,
    longestStreak: 0,
    currentStreak: 0,
    totalTests: 0,
    totalWpmSum: 0,
    averageWpm: 0,
    highestRank: '🥉 Bronze'
  };
}

function updatePersonalBestRecord(wpm, accuracy) {
  const record = getPersonalBestRecord();
  record.totalTests = (record.totalTests || 0) + 1;
  record.totalWpmSum = (record.totalWpmSum || 0) + wpm;
  record.averageWpm = Math.round(record.totalWpmSum / record.totalTests);
  record.bestWpm = Math.max(record.bestWpm || 0, wpm);
  record.bestAccuracy = Math.max(record.bestAccuracy || 0, accuracy);

  if (wpm >= 40 && accuracy >= 85) {
    record.currentStreak = (record.currentStreak || 0) + 1;
    record.longestStreak = Math.max(record.longestStreak || 0, record.currentStreak);
  } else {
    record.currentStreak = 0;
  }

  if (record.bestWpm >= 110) record.highestRank = '🚀 Master';
  else if (record.bestWpm >= 90) record.highestRank = '👑 Diamond';
  else if (record.bestWpm >= 70) record.highestRank = '💎 Platinum';
  else if (record.bestWpm >= 50) record.highestRank = '🥇 Gold';
  else if (record.bestWpm >= 30) record.highestRank = '🥈 Silver';
  else record.highestRank = '🥉 Bronze';

  localStorage.setItem('typeMaster_personalBest', JSON.stringify(record));
  return record;
}

/* ==========================================================================
   WEB AUDIO API SOUND FX SYNTHESIZER ENGINE (Zero Network Latency)
   ========================================================================== */

let audioCtx = null;

function getAudioContext() {
  if (!audioCtx) {
    const AudioContextClass = window.AudioContext || window.webkitAudioContext;
    if (AudioContextClass) {
      audioCtx = new AudioContextClass();
    }
  }
  if (audioCtx && audioCtx.state === 'suspended') {
    audioCtx.resume();
  }
  return audioCtx;
}

function isSoundEnabled() {
  const val = localStorage.getItem('typeMaster_soundEnabled');
  return val === null ? true : val === 'true';
}

function toggleSoundFX() {
  const current = isSoundEnabled();
  const next = !current;
  localStorage.setItem('typeMaster_soundEnabled', String(next));
  updateSoundToggleButton();
  if (next) playSoundFX('keypress');
  return next;
}

function updateSoundToggleButton() {
  const btn = document.getElementById('sound-toggle-btn');
  if (!btn) return;
  const enabled = isSoundEnabled();
  btn.innerHTML = enabled ? '🔊 Sound FX: ON' : '🔇 Sound FX: OFF';
  btn.classList.toggle('active', enabled);
}

function playSoundFX(type) {
  if (!isSoundEnabled()) return;
  try {
    const ctx = getAudioContext();
    if (!ctx) return;

    const now = ctx.currentTime;

    if (type === 'keypress') {
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();
      osc.type = 'sine';
      osc.frequency.setValueAtTime(600, now);
      osc.frequency.exponentialRampToValueAtTime(200, now + 0.03);
      gain.gain.setValueAtTime(0.08, now);
      gain.gain.exponentialRampToValueAtTime(0.001, now + 0.03);
      osc.connect(gain);
      gain.connect(ctx.destination);
      osc.start(now);
      osc.stop(now + 0.03);
    } else if (type === 'error') {
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();
      osc.type = 'sawtooth';
      osc.frequency.setValueAtTime(140, now);
      osc.frequency.linearRampToValueAtTime(70, now + 0.08);
      gain.gain.setValueAtTime(0.12, now);
      gain.gain.linearRampToValueAtTime(0.001, now + 0.08);
      osc.connect(gain);
      gain.connect(ctx.destination);
      osc.start(now);
      osc.stop(now + 0.08);
    } else if (type === 'completion') {
      const freqs = [523.25, 659.25, 783.99, 1046.50];
      freqs.forEach((freq, index) => {
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();
        const startTime = now + index * 0.07;
        osc.type = 'triangle';
        osc.frequency.setValueAtTime(freq, startTime);
        gain.gain.setValueAtTime(0.1, startTime);
        gain.gain.exponentialRampToValueAtTime(0.001, startTime + 0.2);
        osc.connect(gain);
        gain.connect(ctx.destination);
        osc.start(startTime);
        osc.stop(startTime + 0.2);
      });
    } else if (type === 'achievement') {
      const freqs = [440, 554.37, 659.25, 880];
      freqs.forEach((freq, index) => {
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();
        const startTime = now + index * 0.09;
        osc.type = 'sine';
        osc.frequency.setValueAtTime(freq, startTime);
        gain.gain.setValueAtTime(0.12, startTime);
        gain.gain.exponentialRampToValueAtTime(0.001, startTime + 0.25);
        osc.connect(gain);
        gain.connect(ctx.destination);
        osc.start(startTime);
        osc.stop(startTime + 0.25);
      });
    }
  } catch (e) {
    console.log('Audio FX error:', e);
  }
}

/* ==========================================================================
   REAL-TIME WEBSOCKET AUTO-UPDATE & BROADCAST SYNC ENGINE
   ========================================================================== */

let wsClient = null;
let wsReconnectTimer = null;
const realtimeChannel = typeof BroadcastChannel !== 'undefined' ? new BroadcastChannel('typeMaster_realtime') : null;

function initWebSocketRealtimeSync() {
  const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
  const wsUrl = `${protocol}//${window.location.host}/ws/results`;

  try {
    wsClient = new WebSocket(wsUrl);

    wsClient.onopen = () => {
      console.log('⚡ Connected to TypeMaster WebSocket Server for real-time auto-updates.');
      if (wsReconnectTimer) {
        clearTimeout(wsReconnectTimer);
        wsReconnectTimer = null;
      }
    };

    wsClient.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        handleRealtimeEvent(data);
      } catch (e) {}
    };

    wsClient.onclose = () => {
      scheduleWebSocketReconnect();
    };

    wsClient.onerror = () => {
      if (wsClient) wsClient.close();
    };
  } catch (err) {
    scheduleWebSocketReconnect();
  }
}

function scheduleWebSocketReconnect() {
  if (!wsReconnectTimer) {
    wsReconnectTimer = setTimeout(() => {
      wsReconnectTimer = null;
      initWebSocketRealtimeSync();
    }, 4000);
  }
}

function handleRealtimeEvent(data) {
  if (data.type === 'NEW_RESULT') {
    if (realtimeChannel) {
      try { realtimeChannel.postMessage(data); } catch (e) {}
    }
    if (typeof renderGlobalLeaderboard === 'function') renderGlobalLeaderboard();
    if (typeof renderMyHistory === 'function') renderMyHistory();
  }
}

if (realtimeChannel) {
  realtimeChannel.onmessage = (event) => {
    if (event.data && event.data.type === 'NEW_RESULT') {
      if (typeof renderGlobalLeaderboard === 'function') renderGlobalLeaderboard();
      if (typeof renderMyHistory === 'function') renderMyHistory();
    }
  };
}

// Run route protection, navigation initialization, splash preloader, and theme listeners on DOM ready
document.addEventListener('DOMContentLoaded', () => {
  initSplashScreen();
  enforceAuthGuard();
  initNavigation();
  updateSoundToggleButton();
  initWebSocketRealtimeSync();
  initThemeShowcase();
});
