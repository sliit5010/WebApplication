const API_BASE = '/api';
let currentPackages = [];

// Navigation
function showSection(sectionId) {
    document.querySelectorAll('section').forEach(sec => {
        sec.classList.remove('section-active');
        sec.classList.add('section-hidden');
    });
    
    document.getElementById(sectionId).classList.remove('section-hidden');
    document.getElementById(sectionId).classList.add('section-active');

    document.querySelectorAll('.nav-links a').forEach(link => {
        link.classList.remove('active');
    });
    event.target.classList.add('active');

    if (sectionId === 'my-bookings') fetchBookings();
    if (sectionId === 'packages') fetchAdminPackages();
    if (sectionId === 'payments') {
        initializePaymentForm();
        fetchPayments();
    }
    if (sectionId === 'admin') fetchAdminStats();
}

async function fetchAdminStats() {
    try {
        const response = await fetch(`${API_BASE}/admin/stats?_t=${Date.now()}`);
        if(response.ok) {
            const stats = await response.json();
            document.getElementById('stat-users').innerText = stats.totalUsers || 0;
            document.getElementById('stat-photographers').innerText = stats.totalPhotographers || 0;
            document.getElementById('stat-bookings').innerText = stats.totalBookings || 0;
            document.getElementById('stat-packages').innerText = stats.totalPackages || 0;
            document.getElementById('stat-revenue').innerText = 'Rs ' + (stats.totalRevenue ? stats.totalRevenue.toFixed(2) : '0.00');
        }
    } catch(e) {
        console.error("Error fetching admin stats", e);
    }
}

// Notifications
function showNotification(message, isError = false) {
    const notif = document.getElementById('notification');
    notif.querySelector('#notif-message').innerText = message;
    notif.style.background = isError ? 'linear-gradient(135deg, #ef4444, #b91c1c)' : 'linear-gradient(135deg, #10b981, #059669)';
    notif.classList.add('show');
    setTimeout(() => {
        notif.classList.remove('show');
    }, 3000);
}

// Modals
function openModal(modalId) {
    document.getElementById(modalId).style.display = 'flex';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// --- PAYMENT MANAGEMENT (MEMBER 5) ---

function initializePaymentForm() {
    const paymentDate = document.getElementById('payment-date');
    if (paymentDate && !paymentDate.value) {
        paymentDate.value = new Date().toISOString().slice(0, 10);
    }
    togglePaymentFields();
}

function togglePaymentFields() {
    const paymentType = document.getElementById('payment-type')?.value || 'CARD';
    const cardFields = document.getElementById('card-payment-fields');
    const cashFields = document.getElementById('cash-payment-fields');

    if (!cardFields || !cashFields) return;

    cardFields.style.display = paymentType === 'CARD' ? 'block' : 'none';
    cashFields.style.display = paymentType === 'CASH' ? 'block' : 'none';
}

function showPaymentAlert(message, isError = true) {
    const alert = document.getElementById('payment-alert');
    if (!alert) return;

    alert.textContent = message;
    alert.classList.remove('d-none', 'alert-success', 'alert-danger');
    alert.classList.add(isError ? 'alert-danger' : 'alert-success');
}

function hidePaymentAlert() {
    const alert = document.getElementById('payment-alert');
    if (alert) {
        alert.classList.add('d-none');
    }
}

function getPaymentFormData() {
    return {
        paymentId: document.getElementById('payment-id').value,
        customerName: document.getElementById('payment-customer-name').value.trim(),
        bookingId: document.getElementById('payment-booking-id').value.trim(),
        paymentType: document.getElementById('payment-type').value,
        amount: parseFloat(document.getElementById('payment-amount').value),
        paymentDate: document.getElementById('payment-date').value,
        paymentStatus: document.getElementById('payment-status').value,
        cardHolderName: document.getElementById('card-holder-name').value.trim(),
        cardNumber: document.getElementById('card-number').value.trim(),
        expiryDate: document.getElementById('expiry-date').value,
        cvv: document.getElementById('cvv').value.trim(),
        cardType: document.getElementById('card-type').value,
        receiptNumber: document.getElementById('receipt-number').value.trim(),
        paidLocation: document.getElementById('paid-location').value.trim()
    };
}

function validatePaymentForm(payment) {
    if (!payment.customerName) return 'Customer name cannot be empty.';
    if (!payment.bookingId) return 'Booking ID cannot be empty.';
    if (!payment.amount || payment.amount <= 0) return 'Amount must be greater than 0.';
    if (!['CARD', 'CASH'].includes(payment.paymentType)) return 'Payment type must be either CARD or CASH.';
    if (!['PENDING', 'COMPLETED', 'FAILED'].includes(payment.paymentStatus)) {
        return 'Payment status must be PENDING, COMPLETED, or FAILED.';
    }
    if (payment.paymentType === 'CARD') {
        if (!payment.cardHolderName) return 'Card holder name must not be empty.';
        if (!/^\d{16}$/.test(payment.cardNumber)) return 'Card number must contain exactly 16 digits.';
        if (!payment.expiryDate) return 'Expiry date must not be empty.';
        if (!/^\d{3}$/.test(payment.cvv)) return 'CVV must contain exactly 3 digits.';
    }
    if (payment.paymentType === 'CASH' && !payment.receiptNumber) {
        return 'Receipt number must not be empty for cash payment.';
    }
    return '';
}

async function savePayment(e) {
    e.preventDefault();
    hidePaymentAlert();

    const payment = getPaymentFormData();
    const validationMessage = validatePaymentForm(payment);
    if (validationMessage) {
        showPaymentAlert(validationMessage);
        return;
    }

    const isUpdate = Boolean(payment.paymentId);
    const url = isUpdate ? `${API_BASE}/payments/${payment.paymentId}` : `${API_BASE}/payments`;
    const method = isUpdate ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payment)
        });
        const result = await response.json();
        if (!response.ok) {
            showPaymentAlert(result.message || 'Error saving payment record.');
            return;
        }

        showPaymentAlert(isUpdate ? 'Payment updated successfully.' : 'Payment added successfully.', false);
        showNotification(isUpdate ? 'Payment updated successfully' : 'Payment added successfully');
        resetPaymentForm();
        fetchPayments();
    } catch (error) {
        showPaymentAlert('Error saving payment record.');
    }
}

async function fetchPayments() {
    try {
        const response = await fetch(`${API_BASE}/payments?_t=${Date.now()}`);
        const payments = await response.json();
        const tbody = document.getElementById('payments-tbody');
        if (!tbody) return;

        tbody.innerHTML = '';
        if (payments.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center">No payment records found.</td></tr>';
            return;
        }

        payments.forEach(payment => {
            const detail = payment.paymentType === 'CARD'
                ? `${payment.cardType || 'CARD'} ${payment.maskedCardNumber || ''}`
                : `Receipt: ${payment.receiptNumber || '-'}`;
            tbody.innerHTML += `
                <tr>
                    <td>${payment.customerName}</td>
                    <td>${payment.bookingId}</td>
                    <td>${payment.paymentType}</td>
                    <td>Rs ${Number(payment.amount).toFixed(2)}</td>
                    <td>${payment.paymentDate}</td>
                    <td><span class="status-pill status-${payment.paymentStatus.toLowerCase()}">${payment.paymentStatus}</span></td>
                    <td><div class="payment-detail">${detail}</div></td>
                    <td>
                        <button class="btn-edit" onclick="editPayment('${payment.paymentId}')"><i class="fas fa-edit"></i> Edit</button>
                        <button class="btn-danger" onclick="deletePayment('${payment.paymentId}')"><i class="fas fa-trash"></i> Delete</button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        showPaymentAlert('Error loading payment history.');
    }
}

async function editPayment(paymentId) {
    try {
        const response = await fetch(`${API_BASE}/payments/${paymentId}?_t=${Date.now()}`);
        const payment = await response.json();
        if (!response.ok) {
            showPaymentAlert(payment.message || 'Payment record not found.');
            return;
        }

        document.getElementById('payment-form-title').innerText = 'Update Payment';
        document.getElementById('payment-id').value = payment.paymentId;
        document.getElementById('payment-customer-name').value = payment.customerName || '';
        document.getElementById('payment-booking-id').value = payment.bookingId || '';
        document.getElementById('payment-type').value = payment.paymentType || 'CARD';
        document.getElementById('payment-amount').value = payment.amount || '';
        document.getElementById('payment-date').value = payment.paymentDate || '';
        document.getElementById('payment-status').value = payment.paymentStatus || 'PENDING';
        document.getElementById('card-holder-name').value = payment.cardHolderName || '';
        document.getElementById('card-number').value = payment.cardNumber || '';
        document.getElementById('expiry-date').value = payment.expiryDate || '';
        document.getElementById('cvv').value = payment.cvv || '';
        document.getElementById('card-type').value = payment.cardType || 'VISA';
        document.getElementById('receipt-number').value = payment.receiptNumber || '';
        document.getElementById('paid-location').value = payment.paidLocation || '';

        togglePaymentFields();
        document.getElementById('payments').scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        showPaymentAlert('Error loading payment details.');
    }
}

async function deletePayment(paymentId) {
    if (!confirm('Are you sure you want to delete this payment record?')) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/payments/${paymentId}`, { method: 'DELETE' });
        const result = await response.json();
        if (!response.ok) {
            showPaymentAlert(result.message || 'Error deleting payment record.');
            return;
        }

        showPaymentAlert('Payment deleted successfully.', false);
        showNotification('Payment deleted successfully');
        fetchPayments();
    } catch (error) {
        showPaymentAlert('Error deleting payment record.');
    }
}

function resetPaymentForm() {
    document.getElementById('payment-form').reset();
    document.getElementById('payment-id').value = '';
    document.getElementById('payment-form-title').innerText = 'Add Payment';
    hidePaymentAlert();
    initializePaymentForm();
}

// --- PACKAGE MANAGEMENT (MEMBER 4) ---

// Load packages for user view
async function loadPackages(eventType) {
    try {
        const response = await fetch(`${API_BASE}/packages?eventType=${eventType}&_t=${Date.now()}`);
        const packages = await response.json();
        currentPackages = packages;
        
        const container = document.getElementById('packages-container');
        const list = document.getElementById('packages-list');
        document.getElementById('package-subtitle').innerHTML = `Select a package for your <b>${eventType}</b> event`;
        
        list.innerHTML = '';
        
        if (packages.length === 0) {
            list.innerHTML = '<p>No packages found for this event type.</p>';
        } else {
            packages.forEach(pkg => {
                const card = document.createElement('div');
                card.className = 'package-card';
                card.innerHTML = `
                    <h3>${pkg.name}</h3>
                    <div class="package-price">Rs ${pkg.price}</div>
                    <div class="package-details">${pkg.details}</div>
                    <button class="btn-primary" onclick="openBookingModal('${pkg.id}', '${pkg.name}', '${pkg.eventType}')">Book Now</button>
                `;
                list.appendChild(card);
            });
        }
        
        container.style.display = 'block';
        
        // Scroll to packages smoothly
        container.scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        showNotification('Error loading packages', true);
    }
}

// Admin: Fetch all packages
async function fetchAdminPackages() {
    try {
        const response = await fetch(`${API_BASE}/packages?_t=${Date.now()}`);
        const packages = await response.json();
        const tbody = document.getElementById('packages-admin-tbody');
        tbody.innerHTML = '';
        
        packages.forEach(pkg => {
            tbody.innerHTML += `
                <tr>
                    <td>${pkg.name}</td>
                    <td>${pkg.eventType}</td>
                    <td>${pkg.details}</td>
                    <td>Rs ${pkg.price}</td>
                    <td>
                        <button class="btn-edit" onclick="editPackage('${pkg.id}')"><i class="fas fa-edit"></i> Edit</button>
                        <button class="btn-danger" onclick="deletePackage('${pkg.id}')"><i class="fas fa-trash"></i> Delete</button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        showNotification('Error fetching packages', true);
    }
}

// Admin: Open modal to add new package
function openPackageModal() {
    document.getElementById('package-form').reset();
    document.getElementById('pkg-id').value = '';
    document.getElementById('package-modal-title').innerText = 'Add New Package';
    openModal('package-modal');
}

// Admin: Edit package
async function editPackage(id) {
    try {
        const response = await fetch(`${API_BASE}/packages/${id}?_t=${Date.now()}`);
        const pkg = await response.json();
        
        document.getElementById('pkg-id').value = pkg.id;
        document.getElementById('pkg-name').value = pkg.name;
        document.getElementById('pkg-type').value = pkg.eventType;
        document.getElementById('pkg-details').value = pkg.details;
        document.getElementById('pkg-price').value = pkg.price;
        
        document.getElementById('package-modal-title').innerText = 'Edit Package';
        openModal('package-modal');
    } catch (error) {
        showNotification('Error loading package details', true);
    }
}

// Admin: Save package (Create or Update)
async function savePackage(e) {
    e.preventDefault();
    
    const id = document.getElementById('pkg-id').value;
    const pkgData = {
        name: document.getElementById('pkg-name').value,
        eventType: document.getElementById('pkg-type').value,
        details: document.getElementById('pkg-details').value,
        price: parseFloat(document.getElementById('pkg-price').value)
    };
    
    try {
        if (id) {
            // Update
            await fetch(`${API_BASE}/packages/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(pkgData)
            });
            showNotification('Package updated successfully');
        } else {
            // Create
            await fetch(`${API_BASE}/packages`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(pkgData)
            });
            showNotification('Package added successfully');
        }
        closeModal('package-modal');
        fetchAdminPackages();
    } catch (error) {
        showNotification('Error saving package', true);
    }
}

// Admin: Delete package
async function deletePackage(id) {
    if(confirm('Are you sure you want to delete this package?')) {
        try {
            await fetch(`${API_BASE}/packages/${id}`, { method: 'DELETE' });
            showNotification('Package deleted successfully');
            fetchAdminPackages();
        } catch (error) {
            showNotification('Error deleting package', true);
        }
    }
}

// --- BOOKING MANAGEMENT (MEMBER 3) ---

function openBookingModal(pkgId, pkgName, eventType) {
    document.getElementById('booking-form').reset();
    document.getElementById('book-package-id').value = pkgId;
    document.getElementById('booking-package-details').innerText = `Booking: ${pkgName} for ${eventType} Event`;
    openModal('booking-modal');
}

// Submit new booking
async function submitBooking(e) {
    e.preventDefault();
    
    const bookingData = {
        customerName: document.getElementById('book-name').value,
        customerEmail: document.getElementById('book-email').value,
        packageId: document.getElementById('book-package-id').value,
        eventDate: document.getElementById('book-date').value
    };
    
    try {
        await fetch(`${API_BASE}/bookings`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bookingData)
        });
        showNotification('Booking confirmed successfully!');
        closeModal('booking-modal');
    } catch (error) {
        showNotification('Error submitting booking', true);
    }
}

// Fetch all bookings
async function fetchBookings() {
    try {
        const response = await fetch(`${API_BASE}/bookings?_t=${Date.now()}`);
        const bookings = await response.json();
        
        // Also fetch packages to show names
        const pkgsResponse = await fetch(`${API_BASE}/packages?_t=${Date.now()}`);
        const packages = await pkgsResponse.json();
        const getPkgName = (id) => {
            const p = packages.find(x => x.id === id);
            return p ? p.name : 'Unknown Package';
        };
        const getPkgEvent = (id) => {
            const p = packages.find(x => x.id === id);
            return p ? p.eventType : 'Unknown';
        };
        const getPkgPrice = (id) => {
            const p = packages.find(x => x.id === id);
            return p ? 'Rs ' + p.price : '-';
        };

        const tbody = document.getElementById('bookings-tbody');
        tbody.innerHTML = '';
        
        bookings.forEach(b => {
            tbody.innerHTML += `
                <tr>
                    <td>${b.customerName}</td>
                    <td>${b.customerEmail}</td>
                    <td>${getPkgEvent(b.packageId)}</td>
                    <td>${getPkgName(b.packageId)}</td>
                    <td>${getPkgPrice(b.packageId)}</td>
                    <td>${b.eventDate}</td>
                    <td>
                        <button class="btn-edit" onclick="editBooking('${b.id}')"><i class="fas fa-edit"></i> Edit</button>
                        <button class="btn-danger" onclick="cancelBooking('${b.id}')"><i class="fas fa-times"></i> Cancel</button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        showNotification('Error fetching bookings', true);
    }
}

// Open Edit Booking Modal
async function editBooking(id) {
    try {
        const response = await fetch(`${API_BASE}/bookings/${id}?_t=${Date.now()}`);
        const booking = await response.json();
        
        document.getElementById('edit-book-id').value = booking.id;
        document.getElementById('edit-book-name').value = booking.customerName;
        document.getElementById('edit-book-email').value = booking.customerEmail;
        document.getElementById('edit-book-date').value = booking.eventDate;
        
        const pkgsResponse = await fetch(`${API_BASE}/packages?_t=${Date.now()}`);
        const packages = await pkgsResponse.json();
        const currentPkg = packages.find(p => p.id === booking.packageId);
        
        if (currentPkg) {
            document.getElementById('edit-book-event-type').value = currentPkg.eventType;
            await loadPackagesForEdit(currentPkg.eventType);
            document.getElementById('edit-book-package').value = booking.packageId;
        } else {
            document.getElementById('edit-book-event-type').value = "Wedding";
            await loadPackagesForEdit("Wedding");
        }
        
        openModal('edit-booking-modal');
    } catch (error) {
        showNotification('Error loading booking details', true);
    }
}

async function loadPackagesForEdit(eventType) {
    try {
        const response = await fetch(`${API_BASE}/packages?eventType=${eventType}&_t=${Date.now()}`);
        const packages = await response.json();
        
        const select = document.getElementById('edit-book-package');
        select.innerHTML = '';
        packages.forEach(pkg => {
            const option = document.createElement('option');
            option.value = pkg.id;
            option.text = `${pkg.name} (Rs ${pkg.price})`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error("Error loading packages for edit", error);
    }
}

// Update booking
async function updateBooking(e) {
    e.preventDefault();
    
    const id = document.getElementById('edit-book-id').value;
    const bookingData = {
        customerName: document.getElementById('edit-book-name').value,
        customerEmail: document.getElementById('edit-book-email').value,
        packageId: document.getElementById('edit-book-package').value,
        eventDate: document.getElementById('edit-book-date').value
    };
    
    try {
        await fetch(`${API_BASE}/bookings/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bookingData)
        });
        showNotification('Booking updated successfully!');
        closeModal('edit-booking-modal');
        fetchBookings();
    } catch (error) {
        showNotification('Error updating booking', true);
    }
}

// Cancel (Delete) booking
async function cancelBooking(id) {
    if(confirm('Are you sure you want to cancel this booking?')) {
        try {
            await fetch(`${API_BASE}/bookings/${id}`, { method: 'DELETE' });
            showNotification('Booking cancelled successfully');
            fetchBookings();
        } catch (error) {
            showNotification('Error cancelling booking', true);
        }
    }
}
