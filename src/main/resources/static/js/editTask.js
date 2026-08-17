const taskTitleField = document.getElementById("taskTitle");
const taskDescriptionField = document.getElementById("taskDescription");
const selectPriorityField = document.getElementById("selectPriority");
const taskDueDateField = document.getElementById("taskDueDate");
const saveTask2 = document.getElementById("saveTask2");

saveTask2.disabled = true;

// Original values when the page loads
const title = taskTitleField.value;
const description = taskDescriptionField.value;
const priority = selectPriorityField.value;
const dueDate = taskDueDateField.value;

// Listen for changes
taskTitleField.addEventListener("input", checkChangedInsertedData);
taskDescriptionField.addEventListener("input", checkChangedInsertedData);
selectPriorityField.addEventListener("change", checkChangedInsertedData);
taskDueDateField.addEventListener("change", checkChangedInsertedData);


function checkChangedInsertedData() {
    const viewTitleField = taskTitleField.value;
    const viewDescriptionField = taskDescriptionField.value;
    const viewPriorityField = selectPriorityField.value;
    const viewDueDateField = taskDueDateField.value;


    if (viewTitleField.trim() === "") {

        saveTask2.disabled = true;
        return;
    }
    if (viewTitleField.trim() !== title || viewDescriptionField.trim() !== description || viewPriorityField !== priority || viewDueDateField !== dueDate ) {
        saveTask2.disabled = false;

    } else {
        saveTask2.disabled = true;
    }
}