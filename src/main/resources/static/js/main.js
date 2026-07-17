function showPassword(){
    let input = document.getElementById("signupPassword");
    let icon = document.getElementById("passwordEyeIcon");

    if("password" === (input.type).trim())
    {
        input.type = "text";
        icon.classList.remove("bi-eye-fill");
        icon.classList.add("bi-eye-slash-fill");
    }
    else if("text" === (input.type).trim()){
        input.type = "password";
        icon.classList.remove("bi-eye-slash-fill");
        icon.classList.add("bi-eye-fill");
    }
}

function showConfirmPassword(){
    let input = document.getElementById("signupConfirmPassword");
    let icon = document.getElementById("confirmPasswordEyeIcon");

    if("password" === (input.type).trim())
    {
        input.type = "text";
        icon.classList.remove("bi-eye-fill");
        icon.classList.add("bi-eye-slash-fill");
    }
    else if("text" === (input.type).trim()){
        input.type = "password";
        icon.classList.remove("bi-eye-slash-fill");
        icon.classList.add("bi-eye-fill");
    }
}

function showSigninPassword(){
    let input = document.getElementById("signinPassword");
    let icon = document.getElementById("signinPasswordEyeIcon");

    if("password" === (input.type).trim())
    {
        input.type = "text";
        icon.classList.remove("bi-eye-fill");
        icon.classList.add("bi-eye-slash-fill");
    }
    else if("text" === (input.type).trim()){
        input.type = "password";
        icon.classList.remove("bi-eye-slash-fill");
        icon.classList.add("bi-eye-fill");
    }
}

const firstPassword = document.getElementById("signupPassword");
const secondPassword = document.getElementById("signupConfirmPassword");
const passwordMessage = document.getElementById("showPasswordMessage");
const signupBtn = document.getElementById("signupBtn");

signupBtn.disabled = true;

secondPassword.addEventListener("input", checkPasswords);
firstPassword.addEventListener("input", checkPasswords);
function checkPasswords() {

    if(firstPassword.value.trim() === "" && secondPassword.value.trim() === "") {
        passwordMessage.innerHTML = "";
        signupBtn.disabled = true;
    }
    else if(firstPassword.value.trim().length > 0 && firstPassword.value.trim().length < 8 || secondPassword.value.trim().length > 0 && secondPassword.value.trim().length < 8){
        passwordMessage.style.color = "red";
        passwordMessage.innerHTML = "Password must have atleast 8 characters";
        signupBtn.disable = true;
    }
    else if(firstPassword.value.trim() === secondPassword.value.trim()) {
        passwordMessage.style.color = "green";
        passwordMessage.innerHTML = "Both passwords are matching";
        signupBtn.disabled = false;

    }
    else {
        passwordMessage.style.color = "red";
        passwordMessage.innerHTML = "Both passwords must match";
        signupBtn.disabled = true;
    }
}

function searchTask() {
let keyword = document.getElementById("searchInput").value;
fetch("/todos/search?searchword="+encodeURIComponent(keyword))
.then(res => res.text())
.then(data => {
document.getElementById("taskContainer").innerHTML = data;
});
}

function delete_task_button_yes(){
    document.getElementById("deleteTask").click();
    }

function delete_task_button_yess(){
    document.getElementById("deleteTask2").click();
    }

function delete_task_button_no(){
      return false;
    }

function save_task_button_yes(){
      document.getElementById("saveTask").click();
      }

function save_task_button_no(){
       return false;
      }

function add_task_button_yes(){
    document.getElementById("addNewTask").click();
}

function add_task_button_no(){
    return false;
}


function showLoader() {
    document.getElementById("loaderOverlay").style.display = "flex";
    return true;
}

function hideLoader(){
document.getElementById("signinUsername").value = "";
document.getElementById("signinPassword").value = "";
document.getElementById("loaderOverlay").style.display = "none";
}

