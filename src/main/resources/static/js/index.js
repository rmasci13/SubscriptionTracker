document.addEventListener("DOMContentLoaded", function() {
    createTable();
});

// Function to populate user's subscription table
function createTable() {
    const contentArea = document.querySelector('.content');
    // API fetch call to publicly accessible route, though only for the current user's DTO
    fetch('/public/api/user/me')
        // Get response from API call, if ok turn into JSON
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        // Take the JSON, extract the subscription array into a variable
        .then(userData => {
            const subscriptions = userData.subscriptions;
            // Create the table
            const table = document.createElement('table');
            table.className = 'table table-dark my-table';

            // Create table header
            const thead = document.createElement('thead');
            thead.innerHTML = `
                <tr>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Cost</th>
                    <th>Billing Cycle</th>
                    <th>Last Payment Date</th>
                    <th>Next Renewal Date</th>
                    <th>Payment Method</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            `;
            // Append table header
            table.appendChild(thead);

            // Create table body
            const tbody = document.createElement('tbody');

            // Populate table with subscription data
            subscriptions.forEach(sub => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${sub.serviceName}</td>
                    <td>${sub.category}</td>
                    <td>$${sub.cost}</td>
                    <td>${sub.billingCycle}</td>
                    <td>${sub.lastPaymentDate}</td>
                    <td>${sub.nextRenewalDate}</td>
                    <td>${sub.paymentMethod}</td>
                    <td>${sub.status}</td>
                    <td>
                        <button class="btn btn-info" data-id="${sub.id}">Edit</button>
                        <button class="btn btn-danger" data-id="${sub.id}">Delete</button>
                    </td>
                `;
                tbody.appendChild(row);
            });
            // Create blank row to add new subscription
            const addRow = document.createElement('tr');
            addRow.innerHTML = `
                    <td><input type="string"></td>
                    <td><select name="Categories"></select></td>
                    <td>$<input type="number" style="width:65px"></td>
                    <td><select name="Billing-Cycle"></select></td>
                    <td><input type="date"></td>
                    <td></td>
                    <td><input type="string"></td>
                    <td><select name="Status"></select></td>
                    <td>
                        <button class="btn btn-success" type="submit">Submit</button>
                    </td>
                `;
            tbody.append(addRow);
            // Fetch enums and populate the selects with options
            fetchAndPopulateEnums();
            // Append the table body to the table
            table.appendChild(tbody);
            // Append the table itself to the content area div
            contentArea.appendChild(table);
        })
        // Catch if error occurred in retrieving user data
        .catch(error => {
            console.error('Error fetching user data:', error);
            contentArea.innerHTML = '<p>Error loading subscription data</p>';
        });
}

// Function to fetch the enums. Calls populate selects function for each select
function fetchAndPopulateEnums() {
    fetch('public/api/enum')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok')
            }
            return response.json();
        })
        .then(enums => {
            populateSelect('[name="Categories"]', enums.categories);
            populateSelect('[name="Billing-Cycle"]', enums.billingCycles);
            populateSelect('[name="Status"]', enums.statuses);
        })
}

// Function to populate the select dropdowns
function populateSelect(selectId, values) {
    const select = document.querySelector(selectId);
    values.forEach(val => {
        const option = document.createElement('option');
        option.value = val;
        option.textContent = val;
        select.appendChild(option);
    })
}