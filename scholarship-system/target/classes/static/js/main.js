/**
 * 高校奖学金管理系统前端脚本
 * 
 * @author System
 * @version 1.0.0
 */

// 全局配置
const API_BASE_URL = '/api';

// 工具函数
const Utils = {
    // 格式化日期
    formatDate(date) {
        if (!date) return '';
        const d = new Date(date);
        return d.toLocaleDateString('zh-CN');
    },

    // 格式化时间
    formatDateTime(datetime) {
        if (!datetime) return '';
        const d = new Date(datetime);
        return d.toLocaleString('zh-CN');
    },

    // 显示消息提示
    showMessage(message, type = 'info') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        alertDiv.style.top = '20px';
        alertDiv.style.right = '20px';
        alertDiv.style.zIndex = '9999';
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        document.body.appendChild(alertDiv);
        
        // 3秒后自动隐藏
        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.parentNode.removeChild(alertDiv);
            }
        }, 3000);
    },

    // 确认对话框
    confirm(message, callback) {
        if (confirm(message)) {
            callback();
        }
    },

    // 防抖函数
    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    },

    // 节流函数
    throttle(func, limit) {
        let inThrottle;
        return function() {
            const args = arguments;
            const context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    },

    // 生成随机ID
    generateId() {
        return Math.random().toString(36).substr(2, 9);
    },

    // 验证邮箱格式
    isValidEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    },

    // 验证手机号格式
    isValidPhone(phone) {
        const re = /^1[3-9]\d{9}$/;
        return re.test(phone);
    }
};

// API请求封装
const ApiClient = {
    // 通用请求方法
    async request(url, options = {}) {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            credentials: 'same-origin'
        };

        const mergedOptions = {
            ...defaultOptions,
            ...options,
            headers: {
                ...defaultOptions.headers,
                ...options.headers
            }
        };

        try {
            const response = await fetch(url, mergedOptions);
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            } else {
                return await response.text();
            }
        } catch (error) {
            console.error('API请求失败:', error);
            Utils.showMessage('网络请求失败，请稍后重试', 'danger');
            throw error;
        }
    },

    // GET请求
    async get(url) {
        return this.request(url, { method: 'GET' });
    },

    // POST请求
    async post(url, data) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },

    // PUT请求
    async put(url, data) {
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },

    // DELETE请求
    async delete(url) {
        return this.request(url, { method: 'DELETE' });
    }
};

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    console.log('高校奖学金管理系统已启动');
    
    // 初始化工具提示
    initializeTooltips();
    
    // 初始化页面功能
    initializePage();
    
    // 加载统计数据
    loadStatistics();
    
    // 初始化搜索功能
    initializeSearch();
    
    // 初始化表单验证
    initializeFormValidation();
});

// 初始化工具提示
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// 初始化页面
function initializePage() {
    // 为所有卡片添加动画效果
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        setTimeout(() => {
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
}

// 加载统计数据
async function loadStatistics() {
    try {
        const data = await ApiClient.get(`${API_BASE_URL}/system/statistics`);
        updateStatistics(data);
    } catch (error) {
        console.error('加载统计数据失败:', error);
    }
}

// 更新统计数据显示
function updateStatistics(data) {
    const elements = {
        studentCount: document.getElementById('studentCount'),
        scholarshipCount: document.getElementById('scholarshipCount'),
        announcementCount: document.getElementById('announcementCount'),
        reviewCount: document.getElementById('reviewCount')
    };

    Object.keys(elements).forEach(key => {
        const element = elements[key];
        if (element && data[key] !== undefined) {
            animateValue(element, 0, data[key], 1000);
        }
    });
}

// 数字动画
function animateValue(element, start, end, duration) {
    const startTimestamp = performance.now();
    const step = (timestamp) => {
        const elapsed = timestamp - startTimestamp;
        const progress = Math.min(elapsed / duration, 1);
        const value = Math.floor(progress * (end - start) + start);
        element.textContent = value.toLocaleString();
        if (progress < 1) {
            window.requestAnimationFrame(step);
        }
    };
    window.requestAnimationFrame(step);
}

// 初始化搜索功能
function initializeSearch() {
    const searchInputs = document.querySelectorAll('.search-input');
    searchInputs.forEach(input => {
        const debouncedSearch = Utils.debounce(performSearch, 300);
        input.addEventListener('input', debouncedSearch);
    });
}

// 执行搜索
async function performSearch(event) {
    const query = event.target.value.trim();
    const searchResults = event.target.nextElementSibling;
    
    if (query.length < 2) {
        if (searchResults) {
            searchResults.style.display = 'none';
        }
        return;
    }

    try {
        const results = await ApiClient.get(`${API_BASE_URL}/search?q=${encodeURIComponent(query)}`);
        displaySearchResults(searchResults, results);
    } catch (error) {
        console.error('搜索失败:', error);
    }
}

// 显示搜索结果
function displaySearchResults(container, results) {
    if (!container) return;
    
    if (results.length === 0) {
        container.innerHTML = '<div class="search-no-results">没有找到匹配的结果</div>';
    } else {
        container.innerHTML = results.map(result => `
            <div class="search-item" onclick="navigateToDetail('${result.type}', '${result.id}')">
                <div class="search-title">${result.title}</div>
                <div class="search-description">${result.description || ''}</div>
            </div>
        `).join('');
    }
    
    container.style.display = 'block';
}

// 导航到详情页面
function navigateToDetail(type, id) {
    window.location.href = `/${type}/detail/${id}`;
}

// 初始化表单验证
function initializeFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
}

// 表单提交处理
async function submitForm(formElement, url, successCallback) {
    const formData = new FormData(formElement);
    const data = Object.fromEntries(formData.entries());
    
    try {
        const response = await ApiClient.post(url, data);
        Utils.showMessage('提交成功！', 'success');
        if (successCallback) {
            successCallback(response);
        }
    } catch (error) {
        console.error('表单提交失败:', error);
        Utils.showMessage('提交失败，请重试', 'danger');
    }
}

// 删除确认
function confirmDelete(id, callback) {
    Utils.confirm('确定要删除这条记录吗？此操作不可撤销。', () => {
        callback(id);
    });
}

// 导出功能
async function exportData(url, filename) {
    try {
        const response = await fetch(url);
        const blob = await response.blob();
        const downloadUrl = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(downloadUrl);
        Utils.showMessage('导出成功！', 'success');
    } catch (error) {
        console.error('导出失败:', error);
        Utils.showMessage('导出失败，请重试', 'danger');
    }
}

// 文件上传
async function uploadFile(file, uploadUrl, progressCallback) {
    return new Promise((resolve, reject) => {
        const formData = new FormData();
        formData.append('file', file);
        
        const xhr = new XMLHttpRequest();
        
        xhr.upload.addEventListener('progress', (e) => {
            if (e.lengthComputable && progressCallback) {
                const percentComplete = (e.loaded / e.total) * 100;
                progressCallback(percentComplete);
            }
        });
        
        xhr.addEventListener('load', () => {
            if (xhr.status === 200) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    resolve(response);
                } catch (e) {
                    reject(e);
                }
            } else {
                reject(new Error(`上传失败: ${xhr.status}`));
            }
        });
        
        xhr.addEventListener('error', () => {
            reject(new Error('网络错误'));
        });
        
        xhr.open('POST', uploadUrl);
        xhr.send(formData);
    });
}

// 页面导航
function navigateToPage(url) {
    window.location.href = url;
}

// 返回上一页
function goBack() {
    window.history.back();
}

// 刷新页面
function refreshPage() {
    window.location.reload();
}

// 窗口大小变化处理
window.addEventListener('resize', Utils.throttle(() => {
    // 处理窗口大小变化
    console.log('窗口大小已改变');
}, 250));

// 导出主要对象供全局使用
window.ScholarshipSystem = {
    Utils,
    ApiClient,
    navigateToPage,
    goBack,
    refreshPage,
    submitForm,
    confirmDelete,
    exportData,
    uploadFile
};