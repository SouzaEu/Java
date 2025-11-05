// SentinelTrack JavaScript Application

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Form validation enhancement
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Search form auto-submit delay
    const searchInputs = document.querySelectorAll('input[name="busca"]');
    searchInputs.forEach(function(input) {
        let timeout;
        input.addEventListener('input', function() {
            clearTimeout(timeout);
            timeout = setTimeout(function() {
                if (input.value.length >= 3 || input.value.length === 0) {
                    input.form.submit();
                }
            }, 500);
        });
    });

    // Confirm delete actions
    const deleteButtons = document.querySelectorAll('.btn-delete-confirm');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            if (!confirm('Tem certeza que deseja excluir este item?')) {
                e.preventDefault();
                return false;
            }
        });
    });

    // Loading states for buttons
    const loadingButtons = document.querySelectorAll('.btn-loading');
    loadingButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const originalText = button.innerHTML;
            button.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Carregando...';
            button.disabled = true;
            
            // Re-enable after 10 seconds as fallback
            setTimeout(function() {
                button.innerHTML = originalText;
                button.disabled = false;
            }, 10000);
        });
    });

    // Format number inputs
    const numberInputs = document.querySelectorAll('input[type="number"][step="0.01"]');
    numberInputs.forEach(function(input) {
        input.addEventListener('blur', function() {
            if (this.value && !isNaN(this.value)) {
                this.value = parseFloat(this.value).toFixed(2);
            }
        });
    });

    // Placa formatting (Brazilian license plate)
    const placaInputs = document.querySelectorAll('input[name="placa"], input[id="placa"]');
    placaInputs.forEach(function(input) {
        input.addEventListener('input', function() {
            let value = this.value.toUpperCase().replace(/[^A-Z0-9]/g, '');
            
            // Format as ABC1234 or ABC1D23
            if (value.length <= 7) {
                if (value.length > 3) {
                    value = value.substring(0, 3) + value.substring(3);
                }
                this.value = value;
            }
        });
        
        input.addEventListener('blur', function() {
            const value = this.value;
            const oldFormat = /^[A-Z]{3}[0-9]{4}$/; // ABC1234
            const newFormat = /^[A-Z]{3}[0-9][A-Z][0-9]{2}$/; // ABC1D23
            
            if (value && !oldFormat.test(value) && !newFormat.test(value)) {
                this.classList.add('is-invalid');
                let feedback = this.parentElement.querySelector('.invalid-feedback');
                if (!feedback) {
                    feedback = document.createElement('div');
                    feedback.className = 'invalid-feedback';
                    this.parentElement.appendChild(feedback);
                }
                feedback.textContent = 'Formato inválido. Use ABC1234 ou ABC1D23';
            } else {
                this.classList.remove('is-invalid');
                const feedback = this.parentElement.querySelector('.invalid-feedback');
                if (feedback) {
                    feedback.remove();
                }
            }
        });
    });

    // Status color coding
    const statusBadges = document.querySelectorAll('.badge[data-status]');
    statusBadges.forEach(function(badge) {
        const status = badge.dataset.status;
        badge.classList.remove('bg-primary', 'bg-success', 'bg-warning', 'bg-danger');
        
        switch(status) {
            case 'DISPONIVEL':
                badge.classList.add('bg-success');
                break;
            case 'EM_USO':
                badge.classList.add('bg-warning');
                break;
            case 'MANUTENCAO':
                badge.classList.add('bg-danger');
                break;
            default:
                badge.classList.add('bg-primary');
        }
    });

    // Table row click navigation
    const clickableRows = document.querySelectorAll('tr[data-href]');
    clickableRows.forEach(function(row) {
        row.style.cursor = 'pointer';
        row.addEventListener('click', function(e) {
            // Don't navigate if clicking on buttons or links
            if (e.target.tagName === 'BUTTON' || e.target.tagName === 'A' || e.target.closest('button') || e.target.closest('a')) {
                return;
            }
            window.location.href = this.dataset.href;
        });
    });

    // Sidebar toggle for mobile
    const sidebarToggle = document.getElementById('sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            document.body.classList.toggle('sidebar-toggled');
        });
    }

    // Print functionality
    const printButtons = document.querySelectorAll('.btn-print');
    printButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            window.print();
        });
    });

    // Copy to clipboard functionality
    const copyButtons = document.querySelectorAll('.btn-copy');
    copyButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const target = document.querySelector(this.dataset.target);
            if (target) {
                navigator.clipboard.writeText(target.textContent).then(function() {
                    // Show success feedback
                    const originalText = button.innerHTML;
                    button.innerHTML = '<i class="bi bi-check"></i> Copiado!';
                    setTimeout(function() {
                        button.innerHTML = originalText;
                    }, 2000);
                });
            }
        });
    });
});

// Utility functions
window.SentinelTrack = {
    // Show toast notification
    showToast: function(message, type = 'info') {
        const toast = document.createElement('div');
        toast.className = `toast align-items-center text-white bg-${type} border-0`;
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        
        let toastContainer = document.querySelector('.toast-container');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
            document.body.appendChild(toastContainer);
        }
        
        toastContainer.appendChild(toast);
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        toast.addEventListener('hidden.bs.toast', function() {
            toast.remove();
        });
    },

    // Format currency
    formatCurrency: function(value) {
        return new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);
    },

    // Format date
    formatDate: function(date) {
        return new Intl.DateTimeFormat('pt-BR').format(new Date(date));
    },

    // Confirm action
    confirm: function(message, callback) {
        if (confirm(message)) {
            callback();
        }
    }
};
