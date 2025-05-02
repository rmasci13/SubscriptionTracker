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
            table.className = 'table table-dark';

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
            const categories = window.appData.categories;
            console.log(categories);
            const statuses = window.appData.status;
            console.log(statuses);
            const billingCycles = window.appData.billingCycles;
            console.log(billingCycles);
            const addRow = document.createElement('tr');
            addRow.innerHTML = `
                    <td><input type="string"></td>
                    <td><select name="Categories"></select></td>
                    <td><input type="number" style="width:50px"></td>
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
            // Create and append options for Category select
            const categorySelect = addRow.querySelector('[name="Categories"]');
            categories.forEach(category => {
                console.log(category);
                const newOption = document.createElement("option");
                newOption.value = category;
                newOption.textContent = category;
                categorySelect.appendChild(newOption);
            });
            // Create and append options for Billing Cycle select
            const billingCycleSelect = addRow.querySelector('[name="Billing-Cycle"]');
            billingCycles.forEach(billingCycle => {
                const newOption = document.createElement("option");
                newOption.value = billingCycle;
                newOption.textContent = billingCycle;
                billingCycleSelect.appendChild(newOption);
            });
            // Create and append options for Status select
            const statusSelect = addRow.querySelector('[name="Status"]');
            statuses.forEach(status => {
                const newOption = document.createElement("option");
                newOption.value = status;
                newOption.textContent = status;
                statusSelect.appendChild(newOption);
            });
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