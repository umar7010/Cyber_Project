// CyberEdu JavaScript Application

// Global variables
let currentPage = 1;
const itemsPerPage = 10;

// Initialize application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    // Add fade-in animation to cards
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
        card.classList.add('fade-in');
    });
    
    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Initialize search functionality
    initializeSearch();
    
    // Initialize file upload
    initializeFileUpload();
}

// Search functionality
function initializeSearch() {
    const searchInputs = document.querySelectorAll('.search-input');
    searchInputs.forEach(input => {
        input.addEventListener('input', debounce(handleSearch, 300));
    });
}

function handleSearch(event) {
    const query = event.target.value.toLowerCase();
    const container = event.target.closest('.search-container');
    const items = container.querySelectorAll('.searchable-item');
    
    items.forEach(item => {
        const text = item.textContent.toLowerCase();
        if (text.includes(query)) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });
}

// File upload functionality
function initializeFileUpload() {
    const fileUploadAreas = document.querySelectorAll('.file-upload-area');
    
    fileUploadAreas.forEach(area => {
        const input = area.querySelector('input[type="file"]');
        
        // Drag and drop events
        area.addEventListener('dragover', handleDragOver);
        area.addEventListener('dragleave', handleDragLeave);
        area.addEventListener('drop', handleDrop);
        
        // Click to upload
        area.addEventListener('click', () => input.click());
        
        // File selection
        input.addEventListener('change', handleFileSelect);
    });
}

function handleDragOver(e) {
    e.preventDefault();
    e.currentTarget.classList.add('dragover');
}

function handleDragLeave(e) {
    e.preventDefault();
    e.currentTarget.classList.remove('dragover');
}

function handleDrop(e) {
    e.preventDefault();
    e.currentTarget.classList.remove('dragover');
    
    const files = e.dataTransfer.files;
    if (files.length > 0) {
        const input = e.currentTarget.querySelector('input[type="file"]');
        input.files = files;
        handleFileSelect({ target: input });
    }
}

function handleFileSelect(e) {
    const file = e.target.files[0];
    if (file) {
        const area = e.target.closest('.file-upload-area');
        const fileName = area.querySelector('.file-name');
        const fileSize = area.querySelector('.file-size');
        
        if (fileName) fileName.textContent = file.name;
        if (fileSize) fileSize.textContent = formatFileSize(file.size);
        
        area.classList.add('file-selected');
    }
}

function formatFileSize(bytes) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

// API Helper functions
async function apiRequest(url, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
        },
    };
    
    const config = { ...defaultOptions, ...options };
    
    try {
        const response = await fetch(url, config);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('API request failed:', error);
        throw error;
    }
}

// Glossary specific functions
function searchGlossary(query) {
    return apiRequest(`/api/glossary/search?query=${encodeURIComponent(query)}`);
}

function getGlossaryByCategory(category) {
    return apiRequest(`/api/glossary/category/${encodeURIComponent(category)}`);
}

// Learning specific functions
function getLearningModules() {
    return apiRequest('/api/learning/modules');
}

function getLearningModule(id) {
    return apiRequest(`/api/learning/modules/${id}`);
}

function getModuleSteps(moduleId) {
    return apiRequest(`/api/learning/modules/${moduleId}/steps`);
}

// News specific functions
function getNewsArticles() {
    return apiRequest('/api/news');
}

function searchNews(query) {
    return apiRequest(`/api/news/search?query=${encodeURIComponent(query)}`);
}

// Cheat sheets specific functions
function getCheatSheets() {
    return apiRequest('/api/cheatsheets');
}

function getCheatSheetsByCategory(category) {
    return apiRequest(`/api/cheatsheets/category/${encodeURIComponent(category)}`);
}

// Inquiry functions
function submitInquiry(inquiryData) {
    return apiRequest('/api/inquiry', {
        method: 'POST',
        body: JSON.stringify(inquiryData)
    });
}

// Utility functions
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    notification.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(notification);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (notification.parentNode) {
            notification.parentNode.removeChild(notification);
        }
    }, 5000);
}

function showLoading(element) {
    element.classList.add('loading');
    const spinner = document.createElement('div');
    spinner.className = 'spinner-border spinner-border-sm me-2';
    element.prepend(spinner);
}

function hideLoading(element) {
    element.classList.remove('loading');
    const spinner = element.querySelector('.spinner-border');
    if (spinner) {
        spinner.remove();
    }
}

// Form validation
function validateForm(form) {
    const requiredFields = form.querySelectorAll('[required]');
    let isValid = true;
    
    requiredFields.forEach(field => {
        if (!field.value.trim()) {
            field.classList.add('is-invalid');
            isValid = false;
        } else {
            field.classList.remove('is-invalid');
        }
    });
    
    return isValid;
}

// Export functions for global use
window.CyberEdu = {
    apiRequest,
    searchGlossary,
    getGlossaryByCategory,
    getLearningModules,
    getLearningModule,
    getModuleSteps,
    getNewsArticles,
    searchNews,
    getCheatSheets,
    getCheatSheetsByCategory,
    submitInquiry,
    showNotification,
    showLoading,
    hideLoading,
    validateForm,
    debounce
};
