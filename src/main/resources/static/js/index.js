document.addEventListener("DOMContentLoaded", function() {
    createTable();
});

function createTable() {
    const contentArea = document.querySelector('.content');
    fetch('/public/api/user/me')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(userData => {
            console.log(userData);
            const subscriptions = userData.subscriptions;
            console.log(subscriptions);
            // Create and append the table
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
            //Create blank row to add new subscription TO DO
            const addRow = document.createElement('tr');
            addRow.innerHTML = `
                    <td>Blank</td>
                    <td>Blank</td>
                    <td>Blank</td>
                    <td>Blank</td>
                    <td>Blank</td>
                    <td>Blank</td>
                    <td>Blank</td>
                    <td>Blank</td>
                    <td>
                        <button class="btn btn-success" type="submit">Submit</button>
                    </td>
                `;
            table.appendChild(tbody);
            contentArea.appendChild(table);
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            contentArea.innerHTML = '<p>Error loading subscription data</p>';
        });
}