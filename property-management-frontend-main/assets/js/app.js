// Global Frontend Application JS
const API_BASE_URL = 'http://localhost:9291';

// Role Constants
const ROLE_ADMIN = 1;
const ROLE_VENDOR = 2;
const ROLE_CUSTOMER = 3;

// Toast Notifications Helper
function showToast(message, type = 'success') {
    let toastContainer = document.getElementById('toast-container');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toast-container';
        toastContainer.className = 'fixed bottom-4 right-4 z-50 flex flex-col gap-2 pointer-events-none';
        document.body.appendChild(toastContainer);
    }

    const toast = document.createElement('div');
    toast.className = `px-4 py-3 rounded-lg shadow-xl text-white font-medium transform translate-y-2 opacity-0 transition-all duration-300 pointer-events-auto flex items-center gap-2 ${
        type === 'error' ? 'bg-red-600' : type === 'warning' ? 'bg-amber-500' : 'bg-green-600'
    }`;
    
    // Icon
    let icon = '🏡';
    if (type === 'error') icon = '❌';
    else if (type === 'warning') icon = '⚠️';
    else if (type === 'success') icon = '✅';

    toast.innerHTML = `<span>${icon}</span> <span>${message}</span>`;
    toastContainer.appendChild(toast);

    // Animate in
    setTimeout(() => {
        toast.classList.remove('translate-y-2', 'opacity-0');
    }, 10);

    // Dismiss after 4s
    setTimeout(() => {
        toast.classList.add('opacity-0', 'translate-y-2');
        setTimeout(() => {
            toast.remove();
        }, 300);
    }, 4000);
}

// Check local login session
function getAuthToken() {
    return localStorage.getItem('access_token');
}

function setAuth(token, role) {
    localStorage.setItem('access_token', token);
    localStorage.setItem('role', role);
}

function logout() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('role');
    window.location.href = 'login.html';
}

// Decode JWT token payload manually (vanilla JS)
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) {
        return null;
    }
}

function getCurrentUser() {
    const token = getAuthToken();
    if (!token) return null;
    return parseJwt(token);
}

// Role guards to protect dashboard files
function guardPage(allowedRoles) {
    const user = getCurrentUser();
    const role = localStorage.getItem('role');

    if (!user || !role) {
        showToast('Please login to access this page.', 'error');
        setTimeout(() => { window.location.href = 'login.html'; }, 1000);
        return false;
    }

    const currentRole = parseInt(role);
    if (!allowedRoles.includes(currentRole)) {
        showToast('Access denied: Unauthorized dashboard.', 'error');
        setTimeout(() => {
            if (currentRole === ROLE_ADMIN) window.location.href = 'admin-dashboard.html';
            else if (currentRole === ROLE_VENDOR) window.location.href = 'vendor-dashboard.html';
            else window.location.href = 'user-dashboard.html';
        }, 1500);
        return false;
    }
    return true;
}

// API request wrapper
async function fetchApi(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    
    // Set headers
    const headers = options.headers || {};
    const token = getAuthToken();
    if (token) {
        headers['access_token'] = token;
    }

    // Don't set Content-Type if uploading form data (boundary needs to be set automatically by browser)
    if (!(options.body instanceof FormData)) {
        headers['Content-Type'] = 'application/json';
    }

    const fetchOptions = {
        ...options,
        headers: headers
    };

    try {
        const response = await fetch(url, fetchOptions);
        
        // Handle network error status
        if (response.status === 401) {
            showToast('Session expired. Please login again.', 'error');
            logout();
            return null;
        }

        const data = await response.json();
        
        // Handle custom response API contracts
        if (data && (data.responseCode === 400 || data.responseCode === 401)) {
            showToast(data.responseMessage, 'error');
            logout();
            return null;
        }

        return data;
    } catch (error) {
        console.error('API Fetch Error:', error);
        showToast('Failed to connect to the server.', 'error');
        throw error;
    }
}

// UI helper to format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR',
        maximumFractionDigits: 0
    }).format(amount);
}
