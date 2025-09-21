const cartPanel = document.getElementById("cartPanel");
const cartOverlay = document.getElementById("cartOverlay");
const closeCartBtn = document.getElementById("closeCart");
const cartContainerEl = document.getElementById("cartContainer");
const cartTotalEl = document.getElementById("cartTotal");

// Open cart when clicking the cart icon
const cartIcon = document.querySelector("a.nav-link .fa-shopping-cart").closest("a"); // navbar icon
cartIcon.addEventListener("click", (e) => {
    e.preventDefault();
    renderCart();
    cartPanel.classList.add("open");
    cartOverlay.classList.add("show");
});

// Close cart
closeCartBtn.addEventListener("click", () => {
    cartPanel.classList.remove("open");
    cartOverlay.classList.remove("show");
});
cartOverlay.addEventListener("click", () => {
    cartPanel.classList.remove("open");
    cartOverlay.classList.remove("show");
});

// Render cart items
function renderCart() {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];
    let total = 0;

    if (cart.length === 0) {
        cartContainerEl.innerHTML = `
            <div class="text-center mt-5">
                <i class="fas fa-shopping-bag fa-3x text-muted mb-3"></i>
                <p>Your cart is empty</p>
            </div>
        `;
        cartTotalEl.innerText = "0.00";
        return;
    }

    cartContainerEl.innerHTML = cart.map(item => {
        total += item.price * item.quantity;
        return `
        <div class="cart-item">
            <img src="${item.image}" alt="${item.name}">
            <div>
                <h6>${item.name}</h6>
                <p>
                  Rs.${item.price.toFixed(2)}
                  ${item.oldPrice && item.oldPrice > item.price ? `<span class="old-price">Rs.${item.oldPrice.toFixed(2)}</span>` : ''}
                </p>
                <div class="cart-qty">
                    <button onclick="changeQty('${item.id}', -1)">-</button>
                    <span>${item.quantity}</span>
                    <button onclick="changeQty('${item.id}', 1)">+</button>
                </div>
            </div>
            <button class="btn btn-sm btn-danger ms-auto" onclick="removeFromCart('${item.id}')">&times;</button>
        </div>
        `;
    }).join('');

    cartTotalEl.innerText = total.toFixed(2);
}

function changeQty(id, delta) {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    cart = cart.map(item => {
        if (item.id === id) {
            item.quantity = Math.max(1, item.quantity + delta); // prevent going below 1
        }
        return item;
    });
    localStorage.setItem("cart", JSON.stringify(cart));
    renderCart();
    updateCartBadge();
}

// Remove item
function removeFromCart(id) {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    cart = cart.filter(item => item.id !== id);
    localStorage.setItem("cart", JSON.stringify(cart));
    renderCart();
    updateCartBadge();
}

function updateCartBadge() {
    const cart = JSON.parse(localStorage.getItem("cart")) || [];
    const totalQty = cart.reduce((acc, item) => acc + item.quantity, 0);
    document.getElementById("cartBadge").innerText = totalQty;
}

$(document).ready(function () {
    updateCartBadge();
});