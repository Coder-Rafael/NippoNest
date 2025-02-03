async function carregarProdutos() {
    try {
        const response = await fetch('http://localhost:8080/home');
        const produtos = await response.json();

        const productsContainer = document.getElementById('products-container');

        productsContainer.innerHTML = ''; // Limpa o container antes de adicionar novos produtos

        produtos.forEach(produto => {
            const productCard = document.createElement('div');
            productCard.classList.add('product-card');

            // Acesse a primeira imagem da lista (produto.imagens[0])
            const primeiraImagem = produto.imagem[0];

            productCard.innerHTML = `
                <img src="${primeiraImagem}" alt="${produto.nome}">
                <h3>${produto.nome}</h3>
                <p class="price">R$ ${produto.preco.toFixed(2)}</p>
                <div class="product-actions">
                    <button class="add-to-cart">
                        <i class="fas fa-cart-plus"></i>
                    </button>
                    <button class="add-to-favorites">
                        <i class="fas fa-heart"></i> 
                    </button>
                </div>
            `;

            productsContainer.appendChild(productCard);
        });
    } catch (error) {
        console.error('Erro ao carregar os produtos:', error);
    }
}

window.onload = () => {
    carregarProdutos();
};
