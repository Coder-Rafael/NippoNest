function showLogin() {
    document.getElementById('loginForm').classList.remove('d-none');
    document.getElementById('registerForm').classList.add('d-none');
    document.getElementById('recoveryForm').classList.add('d-none');
}

function showRegister() {
    document.getElementById('loginForm').classList.add('d-none');
    document.getElementById('registerForm').classList.remove('d-none');
    document.getElementById('recoveryForm').classList.add('d-none');
}

function showRecovery() {
    document.getElementById('loginForm').classList.add('d-none');
    document.getElementById('registerForm').classList.add('d-none');
    document.getElementById('recoveryForm').classList.remove('d-none');
}

// Login
document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const login = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("http://localhost:8080/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ login, password }),
        });

        if (response.ok) {
            const data = await response.json();
            const token = data.token;
            const userPhoto = data.photo || "http://localhost:8080/uploads/products/product-a4f99d41-20d2-457a-98ad-c683d15a1eae-1.jpeg";
            // Salva os dados no localStorage
            localStorage.setItem("authToken", token);
            localStorage.setItem("userPhoto", userPhoto);

            // Redireciona para a página principal
            window.location.href = "http://localhost:8080";
        } else {
            alert("Erro no login: Verifique suas credenciais");
        }
    } catch (error) {
        console.error("Erro ao fazer login:", error);
        alert("Erro ao conectar ao servidor");
    }
});

// Cadastro
document.getElementById("registerForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const name = document.getElementById("name").value;
    const login = document.getElementById("registerEmail").value;
    const password = document.getElementById("registerPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (password !== confirmPassword) {
        alert("As senhas não coincidem");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ name, login, password }),
        });

        if (response.status === 200) {
            alert("Cadastro bem-sucedido! Faça login para continuar.");
            showLogin(); 
        } else {
            alert("Erro ao cadastrar: Verifique os dados informados");
        }
    } catch (error) {
        console.error("Erro ao fazer cadastro:", error);
        alert("Erro ao conectar ao servidor");
    }
});

// Lógica para manter o login após refresh
document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("authToken");
    const userPhoto = localStorage.getItem("userPhoto"); // Caso você decida armazenar a foto localmente

    if (token && userPhoto) {
        const loginIcon = document.getElementById("loginIcon");
        loginIcon.innerHTML = `<img src="${userPhoto}" alt="User" class="user-photo">`;
    }
});
