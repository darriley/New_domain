let expensesArray = [];

window.onload = function () {
  const storedExpenses = localStorage.getItem('expensesArray');
  if (storedExpenses) {
    expensesArray = JSON.parse(storedExpenses);
    displayAllBudgetBreakdowns();
  }
};

function addExpense() {
  const expenseList = document.getElementById('expense-list');
  const expenseInput = document.createElement('div');
  expenseInput.className = 'expense-input';
  expenseInput.innerHTML = `
    <input type="text" class="expense-name" placeholder="Expense name">
    <input type="number" class="expense-amount" placeholder="Amount">
  `;
  expenseList.appendChild(expenseInput);
}

function calculateBudget() {
  const incomeInput = document.getElementById('income');
  const initialIncome = parseFloat(incomeInput.value) || 0;
  let remainingBudget = initialIncome;

  const currentDate = new Date().toLocaleDateString('en-US');
  const dailyExpenses = [];

  document.querySelectorAll('.expense-input').forEach((expenseInput) => {
    const expenseName = expenseInput.querySelector('.expense-name').value;
    const expenseAmount = parseFloat(expenseInput.querySelector('.expense-amount').value) || 0;

    if (expenseName && expenseAmount) {
      dailyExpenses.push({ name: expenseName, amount: expenseAmount });
      remainingBudget -= expenseAmount;
    }
  });

  const budgetEntry = { date: currentDate, budget: remainingBudget, expenses: dailyExpenses };

  expensesArray.push(budgetEntry);

  displayBudgetBreakdown(budgetEntry);

  // Clear input fields
  incomeInput.value = '';
  document.querySelectorAll('.expense-input').forEach((expenseInput) => {
    expenseInput.querySelector('.expense-name').value = '';
    expenseInput.querySelector('.expense-amount').value = '';
  });

  // Save budget breakdowns to localStorage
  saveToLocalStorage();
}

function saveToLocalStorage() {
  localStorage.setItem('expensesArray', JSON.stringify(expensesArray));
}

function displayBudgetBreakdown(budgetEntry) {
  const budgetBreakdown = document.getElementById('budget-breakdown');

  budgetBreakdown.innerHTML += `<li data-date="${budgetEntry.date}">Date: ${budgetEntry.date}</li>`;
  
  if (budgetEntry.budget >= 0) {
    budgetBreakdown.innerHTML += `<li>Remaining Budget: ₱${budgetEntry.budget.toFixed(2)}</li>`;
  } else {
    budgetBreakdown.innerHTML += `<li>Over Budget: ₱${Math.abs(budgetEntry.budget).toFixed(2)}</li>`;
  }

  if (budgetEntry.expenses.length > 0) {
    budgetBreakdown.innerHTML += '<li>Expense Breakdown:</li>';
    budgetEntry.expenses.forEach((expense) => {
      budgetBreakdown.innerHTML += `<li>${expense.name}: ₱${expense.amount.toFixed(2)}</li>`;
    });
  }
}

function displayAllBudgetBreakdowns() {
  const budgetBreakdown = document.getElementById('budget-breakdown');
  budgetBreakdown.innerHTML = '';

  expensesArray.forEach((budgetEntry) => {
    const breakdownItem = document.createElement('li');
    breakdownItem.setAttribute('data-date', budgetEntry.date);
    breakdownItem.innerHTML = `<li>Date: ${budgetEntry.date}</li>`;

    if (budgetEntry.budget >= 0) {
      breakdownItem.innerHTML += `<li>Remaining Budget: ₱${budgetEntry.budget.toFixed(2)}</li>`;
    } else {
      breakdownItem.innerHTML += `<li>Over Budget: ₱${Math.abs(budgetEntry.budget).toFixed(2)}</li>`;
    }

    if (budgetEntry.expenses.length > 0) {
      breakdownItem.innerHTML += '<li>Expense Breakdown:</li>';
      budgetEntry.expenses.forEach((expense) => {
        breakdownItem.innerHTML += `<li>${expense.name}: ₱${expense.amount.toFixed(2)}</li>`;
      });
    }

    budgetBreakdown.appendChild(breakdownItem);
  });
}

function clearBudgetBreakdowns() {
  const budgetBreakdown = document.getElementById('budget-breakdown');
  budgetBreakdown.innerHTML = '';
  expensesArray = [];
  saveToLocalStorage();
}

function filterByMonth() {
  const selectedMonth = document.getElementById('month-year').value.split('-')[1];
  const budgetBreakdown = document.getElementById('budget-breakdown');

  expensesArray.forEach((budgetEntry) => {
    const entryMonth = budgetEntry.date.split('/')[0].padStart(2, '0'); // Ensure two digits for month

    const breakdownItem = budgetBreakdown.querySelector(`[data-date="${budgetEntry.date}"]`);

    if (breakdownItem) {
      breakdownItem.style.display = entryMonth === selectedMonth ? 'block' : 'none';
    }
  });
}




 function showAll() {
  const budgetBreakdown = document.getElementById('budget-breakdown');
  const breakdownItems = budgetBreakdown.querySelectorAll('[data-date]');
  const selectedMonth = document.getElementById('month-year').value.split('-')[1];

  breakdownItems.forEach((item) => {
    const entryMonth = item.getAttribute('data-date').split('/')[0].padStart(2, '0');
    item.style.display = selectedMonth ? entryMonth === selectedMonth ? 'block' : 'none' : 'block';
  });

  displayAllBudgetBreakdowns(); // Added to display all entries
}


function showSummary() {
  const budgetBreakdown = document.getElementById('budget-breakdown');
  const breakdownItems = budgetBreakdown.querySelectorAll('[data-date]');

  breakdownItems.forEach((item) => {
  const entryMonth = item.getAttribute('data-date').split('/')[0].padStart(2, '0');
  const selectedMonth = document.getElementById('month-year').value.split('-')[1];

  item.style.display = entryMonth === selectedMonth ? 'block' : 'none';
});
}