const deleteAccountField = document.getElementById("accountDeleteConfirmation");
const accountDeletionMessage = document.getElementById("accountDeletionErrorMessage")
const deleteAccountBtn = document.getElementById("deleteAccountButton");

deleteAccountBtn.disabled = true;
accountDeletionMessage.innerHTML = "";
deleteAccountField.addEventListener("input", checkDeleteAccountField);

function checkDeleteAccountField() {

    if(deleteAccountField.value.trim() === "" ) {
        accountDeletionMessage.innerHTML = "";
        deleteAccountBtn.disabled = true;
    }
    else if(deleteAccountField.value.trim() === "Permanently Delete My Account") {
        accountDeletionMessage.style.color = "green";
        accountDeletionMessage.innerHTML = "You can now permanently delete your account";
        deleteAccountBtn.disabled = false;

    }
    else {
        accountDeletionMessage.style.color = "red";
        accountDeletionMessage.innerHTML = "Type the bold deletion statement as it is.";
        deleteAccountBtn.disabled = true;
    }
}

const resetBtn = document.getElementById("deleteAccountResetBtn");
function deleteResetBtn(){
deleteAccountField.value = "";
checkDeleteAccountField();
}
