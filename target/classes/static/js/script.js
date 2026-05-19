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
    const navLink = document.getElementById('nav-' + sectionId);
    if (navLink) {
        navLink.classList.add('active');
    }

    if (sectionId === 'my-bookings') fetchBookings();
    if (sectionId === 'admin') fetchAdminPackages();
    if (sectionId === 'clients' && typeof loadUsers === 'function') loadUsers();

    // Resume slideshow when on home, pause otherwise
    if (sectionId === 'home') {
        if (typeof resetTimer === 'function') resetTimer();
    } else {
        if (typeof autoTimer !== 'undefined') clearInterval(autoTimer);
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
