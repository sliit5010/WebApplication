let photographers = [];

const form = document.getElementById('photographer-form');
const grid = document.getElementById('photographer-grid');
const totalCount = document.getElementById('total-count');
const typeSelect = document.getElementById('type');
const dynamicLabel = document.getElementById('dynamic-label');
const dynamicInput = document.getElementById('dynamic-input');
const submitBtn = document.getElementById('submit-btn');
const cancelBtn = document.getElementById('cancel-btn');
const formTitle = document.getElementById('form-title');
const editIdInput = document.getElementById('edit-id');

typeSelect.addEventListener('change', (e) => {
    if (e.target.value === 'WEDDING') {
        dynamicLabel.textContent = 'Package Type';
        dynamicInput.placeholder = 'e.g. Platinum Package';
    } else {
        dynamicLabel.textContent = 'Event Type';
        dynamicInput.placeholder = 'e.g. Corporate Summit';
    }
});

async function fetchPhotographers() {
    try {
        const res = await fetch('/api/photographers');
        photographers = await res.json();
        renderGrid();
    } catch (e) {
        console.error('Error fetching data', e);
    }
}

function renderGrid() {
    grid.innerHTML = '';
    totalCount.textContent = photographers.length;

    if (photographers.length === 0) {
        grid.innerHTML = `
            <div class="empty-state">
                <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" style="margin-bottom: 1rem; opacity: 0.5;">
                    <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path>
                    <circle cx="12" cy="13" r="4"></circle>
                </svg>
                <h3>No Photographers Found</h3>
                <p>Add your first photographer using the form.</p>
            </div>
        `;
        return;
    }

    photographers.forEach(p => {
        const isWedding = p.type === 'WEDDING';
        const card = document.createElement('div');
        card.className = `card ${isWedding ? 'wedding' : 'event'}`;
        card.innerHTML = `
            <div class="card-header">
                <div>
                    <h3 class="card-title">${p.name}</h3>
                    <div class="card-subtitle">${p.id} • ${p.contact}</div>
                </div>
                <span class="badge ${isWedding ? 'wedding' : 'event'}">${p.type}</span>
            </div>
            <div class="card-body">
                <div class="info-row"><span>Experience</span><span>${p.exp} Years</span></div>
                <div class="info-row"><span>Rate</span><span>$${parseFloat(p.price).toFixed(2)}/hr</span></div>
                <div class="info-row"><span>${isWedding ? 'Package' : 'Event'}</span><span>${p.specialty}</span></div>
            </div>
            <div class="card-actions">
                <button class="btn-action btn-edit" onclick="editPhotographer('${p.id}')">Edit</button>
                <button class="btn-action btn-delete" onclick="deletePhotographer('${p.id}')">Delete</button>
            </div>
        `;
        grid.appendChild(card);
    });
}

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('id').value;
    const dto = {
        id: id,
        name: document.getElementById('name').value,
        contact: document.getElementById('contact').value,
        exp: parseInt(document.getElementById('exp').value),
        price: parseFloat(document.getElementById('price').value),
        type: typeSelect.value,
        specialty: dynamicInput.value
    };

    const editModeId = editIdInput.value;
    if (editModeId) {
        await fetch(`/api/photographers/${editModeId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dto)
        });
    } else {
        await fetch('/api/photographers', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dto)
        });
    }

    resetForm();
    await fetchPhotographers();
});

window.deletePhotographer = async (id) => {
    if (confirm('Delete photographer?')) {
        await fetch(`/api/photographers/${id}`, { method: 'DELETE' });
        await fetchPhotographers();
    }
};

window.editPhotographer = (id) => {
    const p = photographers.find(photo => photo.id === id);
    if (!p) return;
    document.getElementById('id').value = p.id;
    document.getElementById('id').readOnly = true; 
    document.getElementById('name').value = p.name;
    document.getElementById('contact').value = p.contact;
    document.getElementById('exp').value = p.exp;
    document.getElementById('price').value = p.price;
    typeSelect.value = p.type;
    dynamicInput.value = p.specialty;
    typeSelect.dispatchEvent(new Event('change'));
    editIdInput.value = p.id;
    formTitle.textContent = 'Edit Photographer';
    submitBtn.textContent = 'Update';
    cancelBtn.style.display = 'block';
    document.querySelector('.sidebar').scrollIntoView({ behavior: 'smooth' });
};

cancelBtn.addEventListener('click', resetForm);

function resetForm() {
    form.reset();
    editIdInput.value = '';
    document.getElementById('id').readOnly = false;
    formTitle.textContent = 'Add Photographer';
    submitBtn.textContent = 'Save Photographer';
    cancelBtn.style.display = 'none';
    typeSelect.dispatchEvent(new Event('change'));
}

fetchPhotographers();
