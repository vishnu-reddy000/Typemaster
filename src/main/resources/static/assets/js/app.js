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
  { id: 'dark', name: 'Dark', icon: '🌙' }
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

  // Sync custom dropdown
  const customTrigger = document.getElementById('custom-dropdown-trigger');
  if (customTrigger) {
    const selectedTheme = THEMES.find(t => t.id === themeId);
    if (selectedTheme) {
      const triggerIconText = customTrigger.querySelector('.trigger-icon-text');
      if (triggerIconText) {
        triggerIconText.innerHTML = `${selectedTheme.icon} ${selectedTheme.name}`;
      }
    }
  }
  const customMenu = document.getElementById('custom-dropdown-menu');
  if (customMenu) {
    customMenu.querySelectorAll('.custom-dropdown-item').forEach(item => {
      if (item.dataset.value === themeId) {
        item.classList.add('active');
        item.setAttribute('aria-selected', 'true');
      } else {
        item.classList.remove('active');
        item.setAttribute('aria-selected', 'false');
      }
    });
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
  // Guest users are allowed to access all pages including typing test and results.
  // Authentication is now optional and only needed to persist history to database.
  const currentPath = window.location.pathname.split('/').pop() || 'index.html';
  const protectedPages = ['settings.html', 'settings', 'typing.html', 'typing', 'result.html', 'result'];

  const user = getCurrentUser();

  if (protectedPages.includes(currentPath) && !user) {
    window.location.href = `auth.html?notice=1&redirect=${encodeURIComponent(currentPath)}`;
  }
}

/**
 * Initializes global navigation listeners (mobile toggle, active link highlight, user profile controls).
 */
function injectSettingsLink() {
  const user = getCurrentUser();
  const navMenu = document.getElementById('nav-menu');
  if (navMenu && user && user.username) {
    if (!navMenu.querySelector('a[href="settings.html"]') && !navMenu.querySelector('a[href="settings"]')) {
      const blogLink = navMenu.querySelector('a[href="blog.html"]') || navMenu.querySelector('a[href="blog"]');
      const settingsLi = document.createElement('li');
      settingsLi.innerHTML = '<a href="settings.html">Settings</a>';
      if (blogLink && blogLink.parentElement) {
        navMenu.insertBefore(settingsLi, blogLink.parentElement.nextSibling);
      } else {
        navMenu.appendChild(settingsLi);
      }
    }
  }
}

function initNavigation() {
  const mobileToggle = $('.mobile-toggle');
  const navLinks = $('.nav-links');

  // Inject Settings menu link for logged-in users before menu highlight
  injectSettingsLink();

  if (mobileToggle && navLinks) {
    mobileToggle.addEventListener('click', () => {
      navLinks.classList.toggle('show');
    });
  }

  // Highlight current page nav link based on window.location.pathname
  const currentPath = window.location.pathname.split('/').pop() || 'index.html';
  $$('.nav-links a').forEach(link => {
    const href = link.getAttribute('href');
    if (href === currentPath || (currentPath === '' && href === 'index.html') || 
        (href === 'settings.html' && currentPath === 'settings')) {
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
  const navbarContainer = $('.navbar .container');
  if (!navbarContainer) return;

  // Create or reuse the right-side controls wrapper
  let rightControls = $('#navbar-right-controls');
  if (!rightControls) {
    rightControls = document.createElement('div');
    rightControls.id = 'navbar-right-controls';
    rightControls.className = 'navbar-right-controls';
    const mobileToggle = $('.mobile-toggle');
    if (mobileToggle) {
      navbarContainer.insertBefore(rightControls, mobileToggle);
    } else {
      navbarContainer.appendChild(rightControls);
    }
  }

  // Theme dropdown slot
  let themeSlot = rightControls.querySelector('#theme-nav-container');
  if (!themeSlot) {
    themeSlot = document.createElement('div');
    themeSlot.id = 'theme-nav-container';
    themeSlot.className = 'theme-selector-container';
    rightControls.appendChild(themeSlot);
  }

  // Auth slot
  let authContainer = rightControls.querySelector('#auth-nav-container');
  if (!authContainer) {
    authContainer = document.createElement('div');
    authContainer.id = 'auth-nav-container';
    authContainer.className = 'auth-nav-container';
    rightControls.appendChild(authContainer);
  }

  const user = getCurrentUser();

  if (user && user.username) {
    const avatarHtml = user.profilePicture
      ? `<img src="${user.profilePicture}" alt="Avatar" class="navbar-avatar-img">`
      : `<span class="navbar-avatar-initial">${escapeHtml(user.username.charAt(0).toUpperCase())}</span>`;

    authContainer.innerHTML = `
      <div class="user-badge" style="display: flex; align-items: center; gap: 0.6rem;">
        <a href="settings.html" title="Account Settings" style="display: flex; align-items: center; gap: 0.4rem; text-decoration: none; color: inherit;">
          ${avatarHtml}
          <span class="user-name-text" style="font-weight: 600;">${escapeHtml(user.username)}</span>
        </a>
        <button onclick="logoutUser()" class="btn btn-outline" style="padding: 0.35rem 0.65rem; font-size: 0.82rem;">Logout</button>
      </div>
    `;
  } else {
    authContainer.innerHTML = `
      <div class="auth-buttons">
        <a href="auth.html" class="btn btn-outline" style="padding: 0.4rem 0.85rem; font-size: 0.85rem;">Sign In</a>
        <a href="auth.html?mode=signup" class="btn btn-primary" style="padding: 0.4rem 0.85rem; font-size: 0.85rem;">Sign Up</a>
      </div>
    `;
  }
}

/**
 * Dynamically inserts Theme Selector into header navbar (inside navbar-right-controls).
 */
function renderThemeNavbarSelector() {
  const rightControls = $('#navbar-right-controls');
  if (!rightControls) return;

  let themeContainer = $('#theme-nav-container');
  if (!themeContainer) return;

  const currentTheme = getActiveTheme();
  const selectedTheme = THEMES.find(t => t.id === currentTheme) || THEMES[0];

  themeContainer.innerHTML = `
    <div class="custom-theme-dropdown" id="custom-theme-dropdown">
      <button class="custom-dropdown-trigger" id="custom-dropdown-trigger" aria-haspopup="listbox" aria-expanded="false" aria-label="Select App Theme">
        <span class="trigger-icon-text">${selectedTheme.icon} ${selectedTheme.name}</span>
        <span class="trigger-arrow">▼</span>
      </button>
      <div class="custom-dropdown-menu" id="custom-dropdown-menu" role="listbox">
        ${THEMES.map(t => `
          <div class="custom-dropdown-item ${t.id === currentTheme ? 'active' : ''}" data-value="${t.id}" role="option" aria-selected="${t.id === currentTheme ? 'true' : 'false'}">
            <span>${t.icon} ${t.name}</span>
          </div>
        `).join('')}
      </div>
    </div>
  `;

  const dropdownWrapper = $('#custom-theme-dropdown');
  const trigger = $('#custom-dropdown-trigger');
  const menu = $('#custom-dropdown-menu');

  if (dropdownWrapper && trigger && menu) {
    trigger.addEventListener('click', (e) => {
      e.stopPropagation();
      const isOpen = dropdownWrapper.classList.contains('open');
      if (isOpen) {
        dropdownWrapper.classList.remove('open');
        trigger.setAttribute('aria-expanded', 'false');
      } else {
        dropdownWrapper.classList.add('open');
        trigger.setAttribute('aria-expanded', 'true');
      }
    });

    const items = menu.querySelectorAll('.custom-dropdown-item');
    items.forEach(item => {
      item.addEventListener('click', (e) => {
        const value = item.dataset.value;
        setTheme(value);
        dropdownWrapper.classList.remove('open');
        trigger.setAttribute('aria-expanded', 'false');
      });
    });

    // Close on click outside
    document.addEventListener('click', (e) => {
      if (!dropdownWrapper.contains(e.target)) {
        dropdownWrapper.classList.remove('open');
        trigger.setAttribute('aria-expanded', 'false');
      }
    }, { passive: true });
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

  if (!splashLoader) return;

  const dismissSplash = () => {
    if (splashLoader) {
      splashLoader.style.pointerEvents = 'none';
      splashLoader.style.opacity = '0';
      splashLoader.style.display = 'none';
      splashLoader.classList.add('fade-out');
    }
  };

  // Fail-safe max timeout: Force dismiss after 1.2s max under any condition
  const maxTimeout = setTimeout(dismissSplash, 1200);

  // User interactive dismissal (click, touch, keydown)
  ['click', 'touchstart', 'pointerdown', 'keydown'].forEach(evt => {
    window.addEventListener(evt, () => {
      clearTimeout(maxTimeout);
      dismissSplash();
    }, { once: true, passive: true });
  });

  if (!typewriterTextEl) {
    dismissSplash();
    return;
  }

  const currentPath = window.location.pathname.toLowerCase();
  let fullQuote = "Ideas mean nothing without execution.";

  if (currentPath.includes('typing')) {
    fullQuote = "Work on your typing skills";
  } else if (currentPath.includes('result')) {
    fullQuote = "Analyzing performance...";
  }

  let charIndex = 0;
  typewriterTextEl.textContent = "";

  // Fast Typewriter effect synchronized with progress bar
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
      setTimeout(dismissSplash, 150);
    }
  }, 20);
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
    audioCtx.resume().catch(() => {});
  }
  return audioCtx;
}

// User-gesture unlocker for Web Audio API autoplay policy
function unlockAudioContext() {
  const ctx = getAudioContext();
  if (ctx && ctx.state === 'suspended') {
    ctx.resume().catch(() => {});
  }
}

if (typeof window !== 'undefined') {
  ['click', 'keydown', 'pointerdown', 'touchstart'].forEach(evt => {
    window.addEventListener(evt, unlockAudioContext, { passive: true });
  });
}

function isSoundEnabled() {
  const val = localStorage.getItem('typeMaster_soundEnabled');
  return val === null ? true : val === 'true';
}

function getSoundVolume() {
  const val = localStorage.getItem('typeMaster_soundVolume');
  return val !== null ? parseFloat(val) : 0.35;
}

function setSoundVolume(vol) {
  const num = Math.min(1, Math.max(0.01, parseFloat(vol)));
  localStorage.setItem('typeMaster_soundVolume', String(num));
}

function getSoundPack() {
  return localStorage.getItem('typeMaster_soundPack') || 'mechanical';
}

function setSoundPack(pack) {
  localStorage.setItem('typeMaster_soundPack', pack);
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

function playSoundFX(type, customPack) {
  if (!isSoundEnabled() && type !== 'preview') return;
  try {
    const ctx = getAudioContext();
    if (!ctx) return;
    if (ctx.state === 'suspended') {
      ctx.resume().catch(() => {});
    }

    const now = ctx.currentTime;
    const pack = customPack || getSoundPack();
    const volume = getSoundVolume();

    if (type === 'keypress' || type === 'preview') {
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();

      if (pack === 'typewriter') {
        // Metallic typewriter striker clack
        osc.type = 'sawtooth';
        osc.frequency.setValueAtTime(550, now);
        osc.frequency.exponentialRampToValueAtTime(110, now + 0.045);
        gain.gain.setValueAtTime(volume * 0.85, now);
        gain.gain.exponentialRampToValueAtTime(0.001, now + 0.045);
      } else if (pack === 'digital') {
        // Vibrant bubble pop
        osc.type = 'sine';
        osc.frequency.setValueAtTime(1400, now);
        osc.frequency.exponentialRampToValueAtTime(450, now + 0.03);
        gain.gain.setValueAtTime(volume * 0.75, now);
        gain.gain.exponentialRampToValueAtTime(0.001, now + 0.03);
      } else if (pack === 'minimal') {
        // Clean modern click
        osc.type = 'sine';
        osc.frequency.setValueAtTime(750, now);
        osc.frequency.exponentialRampToValueAtTime(250, now + 0.035);
        gain.gain.setValueAtTime(volume * 0.7, now);
        gain.gain.exponentialRampToValueAtTime(0.001, now + 0.035);
      } else {
        // Default: Mechanical (Cherry MX Clicky Dual Oscillator)
        osc.type = 'triangle';
        osc.frequency.setValueAtTime(950, now);
        osc.frequency.exponentialRampToValueAtTime(180, now + 0.035);
        gain.gain.setValueAtTime(volume * 0.8, now);
        gain.gain.exponentialRampToValueAtTime(0.001, now + 0.035);
      }

      osc.connect(gain);
      gain.connect(ctx.destination);
      osc.start(now);
      osc.stop(now + 0.05);
    } else if (type === 'error') {
      const osc1 = ctx.createOscillator();
      const osc2 = ctx.createOscillator();
      const gain = ctx.createGain();

      osc1.type = 'sawtooth';
      osc2.type = 'square';

      osc1.frequency.setValueAtTime(180, now);
      osc1.frequency.linearRampToValueAtTime(80, now + 0.1);
      osc2.frequency.setValueAtTime(120, now);
      osc2.frequency.linearRampToValueAtTime(60, now + 0.1);

      gain.gain.setValueAtTime(volume * 0.7, now);
      gain.gain.linearRampToValueAtTime(0.001, now + 0.1);

      osc1.connect(gain);
      osc2.connect(gain);
      gain.connect(ctx.destination);

      osc1.start(now);
      osc2.start(now);
      osc1.stop(now + 0.1);
      osc2.stop(now + 0.1);
    } else if (type === 'completion') {
      const freqs = [523.25, 659.25, 783.99, 1046.50];
      freqs.forEach((freq, index) => {
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();
        const startTime = now + index * 0.07;
        osc.type = 'triangle';
        osc.frequency.setValueAtTime(freq, startTime);
        gain.gain.setValueAtTime(volume * 0.7, startTime);
        gain.gain.exponentialRampToValueAtTime(0.001, startTime + 0.25);
        osc.connect(gain);
        gain.connect(ctx.destination);
        osc.start(startTime);
        osc.stop(startTime + 0.25);
      });
    } else if (type === 'achievement') {
      const freqs = [440, 554.37, 659.25, 880];
      freqs.forEach((freq, index) => {
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();
        const startTime = now + index * 0.09;
        osc.type = 'sine';
        osc.frequency.setValueAtTime(freq, startTime);
        gain.gain.setValueAtTime(volume * 0.75, startTime);
        gain.gain.exponentialRampToValueAtTime(0.001, startTime + 0.28);
        osc.connect(gain);
        gain.connect(ctx.destination);
        osc.start(startTime);
        osc.stop(startTime + 0.28);
      });
    }
  } catch (e) {
    console.log('Audio FX error:', e);
  }
}

// Global Window Exports
window.getAudioContext = getAudioContext;
window.unlockAudioContext = unlockAudioContext;
window.isSoundEnabled = isSoundEnabled;
window.getSoundVolume = getSoundVolume;
window.setSoundVolume = setSoundVolume;
window.getSoundPack = getSoundPack;
window.setSoundPack = setSoundPack;
window.toggleSoundFX = toggleSoundFX;
window.updateSoundToggleButton = updateSoundToggleButton;
window.playSoundFX = playSoundFX;

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
  initSandboxSimulator();
});

/* ==========================================================================
   ENTERPRISE SANDBOX SIMULATOR ENGINE (HERO PREVIEW)
   High-performance zero-lag typing engine matching Monkeytype & 10FastFingers
   ========================================================================== */

function initSandboxSimulator() {
  const arenaEl = document.getElementById("sandbox-simulator-arena");
  const inputEl = document.getElementById("sandbox-hidden-input");
  const textEl = document.getElementById("sandbox-mockup-text");
  const wpmEl = document.getElementById("sandbox-wpm");
  const accuracyEl = document.getElementById("sandbox-accuracy");
  const timerEl = document.getElementById("sandbox-timer");
  const badgeEl = document.getElementById("sandbox-floating-badge");
  const badgeTitleEl = document.getElementById("sandbox-badge-title");
  const badgeSubtitleEl = document.getElementById("sandbox-badge-subtitle");
  const resetBtnEl = document.getElementById("sandbox-reset-btn");
  const pauseBtnEl = document.getElementById("sandbox-pause-btn");

  if (!arenaEl || !inputEl || !textEl) return;

  const defaultParagraph = "Developing great typing speed requires practice, rhythm, and accuracy. Master your keyboard skills with live real-time metrics and high performance analytics.";

  let charSpans = [];
  let isStarted = false;
  let isFinished = false;
  let isPaused = false;
  let startTime = 0;
  let elapsedSeconds = 0;
  let timerInterval = null;
  let totalKeystrokes = 0;
  let mistakeCount = 0;

  function renderText() {
    textEl.innerHTML = "";
    charSpans = [];
    const chars = defaultParagraph.split("");
    const fragment = document.createDocumentFragment();

    chars.forEach((char, idx) => {
      const span = document.createElement("span");
      span.textContent = char;
      if (idx === 0) span.className = "active";
      fragment.appendChild(span);
      charSpans.push(span);
    });

    textEl.appendChild(fragment);
  }

  function formatTimer(totalSecs) {
    const mins = Math.floor(totalSecs / 60);
    const secs = totalSecs % 60;
    return `${String(mins).padStart(2, "0")}:${String(secs).padStart(2, "0")}`;
  }

  function updateMetrics(typedVal) {
    totalKeystrokes = typedVal.length;
    let correctCount = 0;
    let errors = 0;

    for (let i = 0; i < charSpans.length; i++) {
      const span = charSpans[i];
      if (i < typedVal.length) {
        if (typedVal[i] === defaultParagraph[i]) {
          span.className = "correct";
          correctCount++;
        } else {
          span.className = "incorrect";
          errors++;
        }
      } else {
        span.className = i === typedVal.length ? "active" : "";
      }
    }

    mistakeCount = errors;

    const now = Date.now();
    const currentElapsedSecs = isStarted && !isPaused ? Math.max(0, Math.floor((now - startTime) / 1000)) : elapsedSeconds;
    const elapsedMins = currentElapsedSecs > 0 ? currentElapsedSecs / 60 : 0;

    let netWpm = 0;
    if (elapsedMins > 0) {
      const rawWpm = (correctCount / 5) / elapsedMins;
      netWpm = Math.max(0, Math.round(rawWpm));
    }

    let accuracy = 100;
    if (totalKeystrokes > 0) {
      accuracy = Math.min(100, Math.max(0, Math.round((correctCount / totalKeystrokes) * 100)));
    }

    if (wpmEl) wpmEl.textContent = isNaN(netWpm) ? "0" : String(netWpm);
    if (accuracyEl) accuracyEl.textContent = isNaN(accuracy) ? "100%" : `${accuracy}%`;
    if (timerEl) timerEl.textContent = formatTimer(currentElapsedSecs);

    return { correctCount, netWpm, accuracy, currentElapsedSecs };
  }

  function startTimer() {
    if (timerInterval) clearInterval(timerInterval);
    startTime = Date.now() - (elapsedSeconds * 1000);
    timerInterval = setInterval(() => {
      if (!isStarted || isFinished || isPaused) return;
      const now = Date.now();
      elapsedSeconds = Math.max(0, Math.floor((now - startTime) / 1000));
      updateMetrics(inputEl.value);
    }, 200);
  }

  function stopTimer() {
    if (timerInterval) {
      clearInterval(timerInterval);
      timerInterval = null;
    }
  }

  function finishTest() {
    isFinished = true;
    stopTimer();
    inputEl.disabled = true;

    const { netWpm, accuracy } = updateMetrics(inputEl.value);

    if (badgeEl) {
      if (badgeTitleEl) badgeTitleEl.textContent = netWpm >= 50 ? "🏆 Speed Demon Unlocked" : "🎯 Practice Completed";
      if (badgeSubtitleEl) badgeSubtitleEl.textContent = `Score: ${netWpm} WPM | ${accuracy}% Accuracy`;
      badgeEl.style.display = "flex";
    }

    if (typeof saveUserHistoryResult === "function") {
      saveUserHistoryResult({
        wpm: netWpm,
        rawWpm: netWpm,
        accuracy: accuracy,
        durationMinutes: 1,
        mode: "PARAGRAPH",
        language: "ENGLISH",
        topic: "GENERAL",
        timestamp: new Date().toISOString()
      });
    }
  }

  function resetSimulator() {
    stopTimer();
    isStarted = false;
    isFinished = false;
    isPaused = false;
    startTime = 0;
    elapsedSeconds = 0;
    totalKeystrokes = 0;
    mistakeCount = 0;

    inputEl.value = "";
    inputEl.disabled = false;

    if (wpmEl) wpmEl.textContent = "0";
    if (accuracyEl) accuracyEl.textContent = "100%";
    if (timerEl) timerEl.textContent = "00:00";
    if (badgeEl) badgeEl.style.display = "none";

    renderText();
    inputEl.focus();
  }

  inputEl.addEventListener("input", () => {
    if (isFinished) return;

    const val = inputEl.value;

    if (!isStarted && val.length > 0) {
      isStarted = true;
      startTime = Date.now();
      startTimer();
    }

    updateMetrics(val);

    if (val.length >= defaultParagraph.length) {
      finishTest();
    }
  });

  arenaEl.addEventListener("click", () => {
    if (!isFinished && inputEl) inputEl.focus();
  });

  if (resetBtnEl) {
    resetBtnEl.addEventListener("click", (e) => {
      e.stopPropagation();
      resetSimulator();
    });
  }

  if (pauseBtnEl) {
    pauseBtnEl.addEventListener("click", (e) => {
      e.stopPropagation();
      if (!isStarted || isFinished) return;
      isPaused = !isPaused;
      if (isPaused) {
        stopTimer();
        pauseBtnEl.style.opacity = "0.5";
      } else {
        startTimer();
        pauseBtnEl.style.opacity = "1";
      }
    });
  }

  renderText();
}

/* ==========================================================================
   USERNAME EDITING HELPERS FOR SETTINGS PAGE
   ========================================================================== */

function openUsernameEdit() {
  const user = getCurrentUser();
  if (!user) return;

  const form = document.getElementById("username-edit-form");
  const input = document.getElementById("new-username-input");
  const alertBox = document.getElementById("username-alert");

  if (!form || !input) return;

  input.value = user.username || "";
  if (alertBox) {
    alertBox.style.display = "none";
    alertBox.className = "username-alert";
    alertBox.textContent = "";
  }

  form.classList.add("open");
  input.focus();
  input.select();
}

function cancelUsernameEdit() {
  const form = document.getElementById("username-edit-form");
  const alertBox = document.getElementById("username-alert");

  if (form) form.classList.remove("open");
  if (alertBox) {
    alertBox.style.display = "none";
    alertBox.textContent = "";
  }
}

async function saveUsername() {
  const user = getCurrentUser();
  if (!user || !user.username) {
    window.location.href = "auth.html?notice=1&redirect=settings.html";
    return;
  }

  const input = document.getElementById("new-username-input");
  const saveBtn = document.getElementById("save-username-btn");
  const alertBox = document.getElementById("username-alert");

  if (!input || !saveBtn || !alertBox) return;

  const newUsername = input.value.trim();
  const currentUsername = user.username;

  alertBox.style.display = "none";
  alertBox.className = "username-alert";

  if (!newUsername) {
    showUsernameAlert("Username cannot be empty.", "error");
    return;
  }

  if (newUsername.length < 3 || newUsername.length > 30) {
    showUsernameAlert("Username must be between 3 and 30 characters.", "error");
    return;
  }

  if (!/^[a-zA-Z0-9_]+$/.test(newUsername)) {
    showUsernameAlert("Username can only contain letters, numbers, and underscores.", "error");
    return;
  }

  if (newUsername === currentUsername) {
    showUsernameAlert("New username is identical to current username.", "error");
    return;
  }

  saveBtn.disabled = true;
  const originalText = saveBtn.textContent;
  saveBtn.textContent = "Saving...";

  try {
    const response = await fetch("/api/auth/change-username", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username: currentUsername,
        newUsername: newUsername
      })
    });

    const data = await response.json();

    if (response.ok && data.success) {
      // Update LocalStorage User session
      user.username = data.username || newUsername;
      localStorage.setItem("typeMaster_user", JSON.stringify(user));

      if (data.token) {
        localStorage.setItem("typeMaster_jwtToken", data.token);
      }

      // Update history username in localStorage
      try {
        const historyData = localStorage.getItem("typeMaster_userHistory");
        if (historyData) {
          let history = JSON.parse(historyData);
          if (Array.isArray(history)) {
            history = history.map(item => {
              if (item.username === currentUsername) {
                item.username = newUsername;
              }
              return item;
            });
            localStorage.setItem("typeMaster_userHistory", JSON.stringify(history));
          }
        }
      } catch (e) {
        console.error("Failed to update local history username:", e);
      }

      // Update DOM text
      const profileUsernameEl = document.getElementById("profile-username");
      if (profileUsernameEl) profileUsernameEl.textContent = newUsername;

      // Update Navbar User Badge
      if (typeof renderAuthNavbar === "function") {
        renderAuthNavbar();
      }

      showUsernameAlert("✓ Username updated successfully!", "success");

      setTimeout(() => {
        cancelUsernameEdit();
      }, 1200);

    } else {
      showUsernameAlert(data.message || "Failed to update username.", "error");
    }
  } catch (err) {
    console.error("Save Username Error:", err);
    showUsernameAlert("Network or server error. Please try again.", "error");
  } finally {
    saveBtn.disabled = false;
    saveBtn.textContent = originalText;
  }
}

function showUsernameAlert(msg, type) {
  const alertBox = document.getElementById("username-alert");
  if (!alertBox) return;
  alertBox.textContent = msg;
  alertBox.className = "username-alert " + type;
  alertBox.style.display = "block";
}

if (typeof window !== "undefined") {
  window.openUsernameEdit = openUsernameEdit;
  window.cancelUsernameEdit = cancelUsernameEdit;
  window.saveUsername = saveUsername;
}
