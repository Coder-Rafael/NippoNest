// Objeto para armazenar produtos carregados no navegador
const produtosCache = {};

async function carregarProdutos() {
    try {
        const response = await fetch('https://nipponest-production.up.railway.app/home');
        if (!response.ok) throw new Error('Erro ao buscar produtos');
        const produtos = await response.json();

        produtos.forEach(produto => {
            produtosCache[produto.id] = produto;
        });

        function createProductCard(produto) {
            return `
            <div class="swiper-slide">
                <div class="product-card" data-product-id="${produto.id}">
                    <div class="product-image">
                        <img src="${produto.imagem.length ? produto.imagem[0] : 'https://placehold.co/200x200?text=Imagem+Indispon%C3%ADvel'}" 
                             alt="${produto.nome}">
                    </div>
                    <div class="product-info">
                        <h3>${produto.nome}</h3>
                        <p class="price">R$ ${produto.preco.toFixed(2)}</p>
                        <p class="location">Belo Horizonte-MG</p>
                    </div>
                </div>
            </div>
        `;
        }

        // Containers dos carrosséis
        const containers = [
            '.products-swiper .swiper-wrapper',
            '.recommendations-swiper .swiper-wrapper',
            '.manga-swiper .swiper-wrapper',
            '.light-novels-swiper .swiper-wrapper'
        ].map(selector => document.querySelector(selector));

        containers.forEach(container => {
            container.innerHTML = produtos.map(produto => createProductCard(produto)).join('');
        });

        // Adiciona eventos de clique uma única vez após renderizar
        containers.forEach(container => {
            container.querySelectorAll('.product-card').forEach(card => {
                card.addEventListener('click', () => {
                    const productId = card.dataset.productId;
                    abrirDetalhesProduto(productId);
                });
            });
        });

        // Inicializa os Swipers
        containers.forEach((_, index) => {
            new Swiper(containers[index].parentElement, {
                slidesPerView: 1,
                spaceBetween: 20,
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                },
                breakpoints: {
                    375: { slidesPerView: 2 },
                    640: { slidesPerView: 3 },
                    768: { slidesPerView: 4 },
                    1024: { slidesPerView: 5 },
                }
            });
        });

    } catch (error) {
        console.error('Erro ao carregar produtos:', error);
    }
}

function criarModal() {
    const modalHTML = `
        <div class="custom-modal-overlay" style="display: none;">
            <div class="custom-product-modal">
                <div class="container-fluid">
                    <button class="custom-close-modal">&times;</button>
                    <div class="row">
                        <div class="col-12 col-md-6">
                            <div class="custom-main-image-container mb-3">
                                <img id="customMainImage" src="" alt="Produto" class="custom-main-image">
                            </div>
                            <div class="custom-thumbnails-container">
                                <div class="row g-2" id="customThumbnails"></div>
                            </div>
                        </div>
                        <div class="col-12 col-md-6">
                            <h1 id="customProductName" class="custom-product-title"></h1>
                            <p class="custom-seller-location">
                                <i class="bi bi-geo-alt"></i>
                                <span id="customSellerLocation"></span>
                            </p>
                            <p id="customProductDescription" class="custom-product-description"></p>
                            <div class="custom-price-container">
                                <span class="custom-currency">R$</span>
                                <span id="customProductPrice" class="custom-price"></span>
                            </div>
                            <div class="custom-buttons-container">
                                <button class="btn btn-primary btn-lg w-100 mb-2">
                                    <i class="bi bi-cart-plus"></i> Adicionar ao Carrinho
                                </button>
                                <button class="btn btn-warning btn-lg w-100">
                                    <i class="bi bi-credit-card"></i> Comprar Agora
                                </button>
                            </div>
                            <div class="custom-social-share mt-4">
                                <p class="text-muted mb-2">Compartilhar:</p>
                                <div class="custom-social-icons">
                                    <a href="#" class="custom-social-icon"><i class="bi bi-facebook"></i></a>
                                    <a href="#" class="custom-social-icon"><i class="bi bi-twitter"></i></a>
                                    <a href="#" class="custom-social-icon"><i class="bi bi-whatsapp"></i></a>
                                    <a href="#" class="custom-social-icon"><i class="bi bi-pinterest"></i></a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="custom-recommended-products mt-5">
                        <h3 class="custom-section-title mb-4">Produtos Recomendados</h3>
                        <div class="custom-products-carousel" id="customRecommendedProducts"></div>
                    </div>
                </div>
            </div>
        </div>
    `;
    document.getElementById('modalContainer').innerHTML = modalHTML;

    // Eventos de fechar
    const modalOverlay = document.querySelector('.custom-modal-overlay');
    const closeModal = document.querySelector('.custom-close-modal');
    
    closeModal.addEventListener('click', () => modalOverlay.style.display = 'none');
    modalOverlay.addEventListener('click', (e) => {
        if (e.target === modalOverlay) modalOverlay.style.display = 'none';
    });
}

    async function abrirDetalhesProduto(productId) {
    if (!document.querySelector('.custom-modal-overlay')) criarModal();

    // Verifica cache ou busca do servidor
    let produto = produtosCache[productId];
    if (!produto) {
        try {
            const response = await fetch(`https://nipponest-production.up.railway.app/home`);
            produto = await response.json();
            produtosCache[productId] = produto; // Atualiza cache
        } catch (error) {
            console.error('Erro ao buscar detalhes:', error);
            return;
        }
    }

    // Preenche dados do modal
    document.getElementById('customProductName').textContent = produto.nome;
    document.getElementById('customProductDescription').textContent = produto.descricao;
    document.getElementById('customProductPrice').textContent = produto.preco.toFixed(2);
    document.getElementById('customSellerLocation').textContent = produto.usuario?.localizacao || 'Belo Horizonte - MG';
    document.getElementById('customMainImage').src = produto.imagem[0] || '';

    // Miniaturas com estrutura correta
    const thumbnails = document.getElementById('customThumbnails');
    thumbnails.innerHTML = produto.imagem.map(img => `
        <div class="col-3">
            <img src="${img}" class="custom-thumbnail-img" alt="Miniatura">
        </div>
    `).join('');

    // Eventos para trocar imagem principal
    thumbnails.querySelectorAll('.custom-thumbnail-img').forEach(img => {
        img.addEventListener('click', () => {
            document.getElementById('customMainImage').src = img.src;
        });
    });

    document.querySelector('.custom-modal-overlay').style.display = 'flex';

    await carregarProdutosRecomendados(productId);
}

async function carregarProdutosRecomendados(productId) {
    const recommendedContainer = document.getElementById('customRecommendedProducts');
    recommendedContainer.innerHTML = ''; // Limpa conteúdo anterior

    try {
        const response = await fetch(`https://nipponest-production.up.railway.app/home`);
        const produtosRecomendados = await response.json();

        if (produtosRecomendados && produtosRecomendados.length) {
            // Estrutura do Swiper
            recommendedContainer.innerHTML = `
                <div class="swiper recommendations-swiper">
                    <div class="swiper-wrapper">
                        ${produtosRecomendados.map(produto => `
                            <div class="swiper-slide">
                                <div class="custom-product-card">
                                    <img src="${produto.imagem[0] || ''}" alt="${produto.nome}" class="custom-product-card-img">
                                    <h4 class="custom-product-card-title">${produto.nome}</h4>
                                    <p class="custom-product-card-price">R$ ${produto.preco.toFixed(2)}</p>
                                    <button class="btn btn-sm btn-primary" onclick="abrirDetalhesProduto(${produto.id})">Ver Detalhes</button>
                                </div>
                            </div>
                        `).join('')}
                    </div>
                    <div class="swiper-button-next"></div>
                    <div class="swiper-button-prev"></div>
                </div>
            `;

            // Inicializa o Swiper
            new Swiper('.recommendations-swiper', {
                slidesPerView: 1,
                spaceBetween: 20,
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                },
                breakpoints: {
                    375: { slidesPerView: 2 },
                    640: { slidesPerView: 3 },
                    768: { slidesPerView: 4 },
                    1024: { slidesPerView: 5 },
                }
            });
        } else {
            recommendedContainer.innerHTML = '<p class="text-muted">Nenhum produto recomendado no momento.</p>';
        }
    } catch (error) {
        console.error('Erro ao carregar produtos recomendados:', error);
        recommendedContainer.innerHTML = '<p class="text-danger">Erro ao carregar recomendações.</p>';
    }
}

/// Atualiza os ícones com base no estado do usuário
document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("authToken");
    const userPhoto = localStorage.getItem("userPhoto");
    const loginIcon = document.getElementById("loginIcon");
    const logoutIcon = document.getElementById("logoutIcon");
    const addProductIcon = document.getElementById("addProductButton");

    if (loginIcon) {
        if (token && userPhoto) {
            // Usuário logado: Exibe a foto do usuário e ícone "Adicionar Produto"
            loginIcon.innerHTML = `<img src="${userPhoto}" alt="User" class="user-photo">`;
            logoutIcon.style.display = "inline"; // Exibe o botão de logout
            if (addProductIcon) addProductIcon.style.display = "inline"; // Exibe o botão "Adicionar Produto"
        } else {
            // Usuário não logado: Exibe o ícone padrão e oculta o "Adicionar Produto"
            loginIcon.innerHTML = `<i class="bi bi-person"></i>`;
            logoutIcon.style.display = "none"; // Oculta o botão de logout
            if (addProductIcon) addProductIcon.style.display = "none"; // Oculta o botão "Adicionar Produto"
        }
    }
});

// Adiciona funcionalidade de logout ao botão
document.getElementById("logoutIcon").addEventListener("click", (e) => {
    e.preventDefault();

    // Remove informações do localStorage
    localStorage.removeItem("authToken");
    localStorage.removeItem("userPhoto");

    // Atualiza os ícones para o estado inicial
    const loginIcon = document.getElementById("loginIcon");
    const logoutIcon = document.getElementById("logoutIcon");
    const addProductIcon = document.getElementById("addProductButton");

    if (loginIcon) {
        loginIcon.innerHTML = `<i class="bi bi-person"></i>`;
    }
    if (logoutIcon) {
        logoutIcon.style.display = "none"; // Oculta o botão de logout
    }
    if (addProductIcon) {
        addProductIcon.style.display = "none"; // Oculta o botão "Adicionar Produto"
    }

    // Redireciona para a página inicial ou de login
    window.location.href = "https://nipponest-production.up.railway.app/";
});

    document.addEventListener("DOMContentLoaded", () => {
        const addProductButton = document.getElementById("addProductButton");
        const addProductModal = document.getElementById("addProductModal");
        const closeModal = document.getElementById("closeModal");
        const addProductForm = document.getElementById("addProductForm");

        // Verifica se os elementos existem
        if (!addProductButton || !addProductModal || !closeModal || !addProductForm) {
            console.error("Erro: Elementos do DOM não encontrados. Verifique os IDs.");
            return;
        }

        // Abre o modal
        addProductButton.addEventListener("click", (e) => {
            e.preventDefault();
            addProductModal.style.display = "flex";
        });

        // Fecha o modal
        closeModal.addEventListener("click", () => {
            addProductModal.style.display = "none";
        });

        // CRIA PRODUTO
        addProductForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            // Cria um objeto FormData
            const formData = new FormData();

            let generosSelecionados = Array.from(document.querySelectorAll("input[name='genero']:checked")).map(e => e.value);

            let tipoProdutoSelecionado = document.querySelector("input[name='tipoProduto']:checked")?.value;
            // Dados do produto
            const productData = {
                nome: document.getElementById("nome").value,
                descricao: document.getElementById("descricao").value,
                preco: parseFloat(document.getElementById("preco").value),
                estoque: parseInt(document.getElementById("estoque").value),
                genero: generosSelecionados,
                tipoProduto: tipoProdutoSelecionado
            };

            // Adiciona o objeto product como JSON dentro de um Blob
            formData.append("product", new Blob([JSON.stringify(productData)], { type: "application/json" }));

            // Adiciona as imagens
            const imagens = document.getElementById("imagens").files;
            for (let i = 0; i < imagens.length; i++) {
                formData.append("imagens", imagens[i]);
            }

            try {
                const token = localStorage.getItem("authToken"); // Ou onde você está armazenando o token
                if (!token) {
                    alert("Usuário não autenticado. Faça login novamente.");
                    return;
                }

                // Envia a requisição
                const response = await fetch("https://nipponest-production.up.railway.app/api/products", {
                    method: "POST",
                    headers: {
                        "Authorization": `Bearer ${token}` // Inclui o Bearer Token
                    },
                    body: formData // Envia os dados do FormData
                });

                if (response.ok) {
                    alert("Produto adicionado com sucesso!");
                    addProductModal.style.display = "none";
                    addProductForm.reset();
                } else {
                    const error = await response.json();
                    alert(`Erro: ${error.message}`);
                }
            } catch (error) {
                console.error("Erro ao adicionar produto:", error);
                alert("Não foi possível adicionar o produto. Tente novamente mais tarde.");
            }
        });
    });

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    carregarProdutos();
});