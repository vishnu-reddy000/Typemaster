/**
 * TypeMaster - Authentication JavaScript (auth.js)
 * Handles Sign In & Sign Up tab switching, API requests, and session creation.
 */

document.addEventListener("DOMContentLoaded", () => {
    // Check URL query parameters for initial state
    const urlParams = new URLSearchParams(window.location.search);
    const mode = urlParams.get("mode");
    const notice = urlParams.get("notice");

    if (mode === "signup") {
        switchAuthTab("signup");
    }

    if (notice) {
        const noticeEl = document.getElementById("auth-notice");
        if (noticeEl) {
            noticeEl.classList.remove("hidden");
        }
    }

    // If user is already logged in, redirect to settings.html
    const currentUser = JSON.parse(localStorage.getItem("typeMaster_user"));
    if (currentUser && currentUser.username) {
        window.location.href = "settings.html";
    }
});

/**
 * Switches between Sign In and Sign Up tabs.
 */
function switchAuthTab(tab) {
    const tabSignIn = document.getElementById("tab-signin");
    const tabSignUp = document.getElementById("tab-signup");
    const formSignIn = document.getElementById("signin-form");
    const formSignUp = document.getElementById("signup-form");
    const alertEl = document.getElementById("auth-alert");

    // Hide alerts when switching tabs
    if (alertEl) {
        alertEl.classList.add("hidden");
        alertEl.textContent = "";
    }

    if (tab === "signup") {
        tabSignIn.classList.remove("active");
        tabSignUp.classList.add("active");
        formSignIn.classList.add("hidden");
        formSignUp.classList.remove("hidden");
    } else {
        tabSignUp.classList.remove("active");
        tabSignIn.classList.add("active");
        formSignUp.classList.add("hidden");
        formSignIn.classList.remove("hidden");
    }
}

/**
 * Handles Sign In Form submission.
 */
async function handleSignIn(event) {
    event.preventDefault();
    const usernameInput = document.getElementById("signin-username").value.trim();
    const passwordInput = document.getElementById("signin-password").value;
    const submitBtn = document.getElementById("signin-submit-btn");

    if (!usernameInput || !passwordInput) {
        showAlert("Please fill in both username/email and password.", "error");
        return;
    }

    setButtonLoading(submitBtn, true, "Signing in...");

    try {
        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username: usernameInput, password: passwordInput })
        });

        const data = await response.json();

        if (response.ok && data.success) {
            showAlert("Login successful! Redirecting...", "success");
            // Store session & JWT token
            localStorage.setItem("typeMaster_user", JSON.stringify({
                id: data.userId,
                username: data.username,
                email: data.email
            }));
            if (data.token) {
                localStorage.setItem("typeMaster_jwtToken", data.token);
            }

            // Redirect after short delay
            setTimeout(() => {
                const urlParams = new URLSearchParams(window.location.search);
                const redirect = urlParams.get("redirect") || "settings.html";
                window.location.href = redirect;
            }, 800);
        } else {
            showAlert(data.message || "Login failed. Invalid credentials.", "error");
        }
    } catch (error) {
        console.error("Sign In Error:", error);
        showAlert("Network or server error. Please try again.", "error");
    } finally {
        setButtonLoading(submitBtn, false, "Sign In →");
    }
}

/**
 * Handles Sign Up Form submission.
 */
async function handleSignUp(event) {
    event.preventDefault();
    const usernameInput = document.getElementById("signup-username").value.trim();
    const emailInput = document.getElementById("signup-email").value.trim();
    const passwordInput = document.getElementById("signup-password").value;
    const submitBtn = document.getElementById("signup-submit-btn");

    if (!usernameInput || !emailInput || !passwordInput) {
        showAlert("Please complete all required fields.", "error");
        return;
    }

    setButtonLoading(submitBtn, true, "Creating Account...");

    try {
        const response = await fetch("/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username: usernameInput, email: emailInput, password: passwordInput })
        });

        const data = await response.json();

        if (response.ok && data.success) {
            // Reset sign up form
            document.getElementById("signup-username").value = "";
            document.getElementById("signup-email").value = "";
            document.getElementById("signup-password").value = "";

            // Switch to Sign In tab
            switchAuthTab("signin");

            // Fill signin username and focus password
            const signinUsernameEl = document.getElementById("signin-username");
            const signinPasswordEl = document.getElementById("signin-password");
            if (signinUsernameEl) {
                signinUsernameEl.value = usernameInput;
            }
            if (signinPasswordEl) {
                signinPasswordEl.focus();
            }

            showAlert(`Account created successfully for '${data.username || usernameInput}'! Please sign in with your credentials.`, "success");
        } else {
            showAlert(data.message || "Registration failed. Please try again.", "error");
        }
    } catch (error) {
        console.error("Sign Up Error:", error);
        showAlert("Network or server error. Please try again.", "error");
    } finally {
        setButtonLoading(submitBtn, false, "Sign Up & Start →");
    }
}

/**
 * Toggles password input visibility between 'password' and 'text'.
 */
function togglePasswordVisibility(inputId, btn) {
    const input = document.getElementById(inputId);
    if (!input) return;

    const eyeIcon = btn.querySelector('.eye-icon');
    if (input.type === 'password') {
        input.type = 'text';
        if (eyeIcon) eyeIcon.textContent = '🙈';
        btn.setAttribute('title', 'Hide password');
    } else {
        input.type = 'password';
        if (eyeIcon) eyeIcon.textContent = '👁️';
        btn.setAttribute('title', 'Show password');
    }
}

/**
 * Helper to display alert messages.
 */
function showAlert(message, type) {
    const alertEl = document.getElementById("auth-alert");
    if (!alertEl) return;

    alertEl.textContent = message;
    alertEl.className = `auth-alert ${type}`;
    alertEl.classList.remove("hidden");
}

/**
 * Helper for button loading states.
 */
function setButtonLoading(btn, isLoading, labelText) {
    if (!btn) return;
    btn.disabled = isLoading;
    btn.innerHTML = isLoading ? `<span>${labelText}</span>` : `<span>${labelText}</span>`;
}
