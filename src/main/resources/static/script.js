async function carregarProdutos() {
    try {
        const response = await fetch('http://localhost:8080/home');
        const produtos = await response.json();

        const productsContainer = document.getElementById('products-container');

        productsContainer.innerHTML = '';

        produtos.forEach(produto => {
            const productCard = document.createElement('div');
            productCard.classList.add('product-card');

            productCard.innerHTML = `
                <img src="${produto.imagem}" alt="${produto.nome}">
                <h3>${produto.nome}</h3>
                <p class="price">R$ ${produto.preco.toFixed(2)}</p>
                <button class="add-to-cart">Adicionar ao Carrinho</button>
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
