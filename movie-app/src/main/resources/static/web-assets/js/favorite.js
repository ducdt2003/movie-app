const API_URL = "/api/favorites";

// 🟥 Thêm phim vào danh sách yêu thích
async function addFavorite(userId, movieId) {
    try {
        const response = await fetch(`${API_URL}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ userId, movieId })
        });

        if (!response.ok) throw new Error("Không thể thêm phim yêu thích.");

        const favorite = await response.json();
        console.log("Đã thêm:", favorite);
        alert("Thêm phim yêu thích thành công!");
    } catch (error) {
        console.error("Lỗi:", error);
        alert("Có lỗi xảy ra khi thêm phim!");
    }
}

// 🟦 Lấy danh sách phim yêu thích của người dùng
async function getFavorites(userId) {
    try {
        const response = await fetch(`${API_URL}/movies?userId=${userId}`);

        if (!response.ok) throw new Error("Không thể lấy danh sách phim.");

        const favorites = await response.json();
        console.log("Danh sách phim yêu thích:", favorites);
        return favorites;
    } catch (error) {
        console.error("Lỗi:", error);
        return [];
    }
}

// 🟧 Xóa một phim yêu thích
async function removeFavorite(userId, movieId) {
    try {
        const response = await fetch(`${API_URL}?userId=${userId}&movieId=${movieId}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" }
        });

        if (!response.ok) throw new Error("Không thể xóa phim yêu thích.");

        console.log("Đã xóa phim:", movieId);
        alert("Xóa phim yêu thích thành công!");
        document.querySelector(`[data-movieId='${movieId}']`).closest(".movie-item").remove();
    } catch (error) {
        console.error("Lỗi:", error);
        alert("Có lỗi xảy ra khi xóa phim!");
    }
}

// 🟥 Xóa toàn bộ phim yêu thích của người dùng
async function removeAllFavorites(userId) {
    try {
        const response = await fetch(`${API_URL}/${userId}/all`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" }
        });

        if (!response.ok) throw new Error("Không thể xóa toàn bộ phim yêu thích.");

        console.log("Đã xóa tất cả phim yêu thích của user:", userId);
        alert("Xóa toàn bộ phim yêu thích thành công!");
        document.querySelectorAll(".movie-item").forEach(item => item.remove());
    } catch (error) {
        console.error("Lỗi:", error);
        alert("Có lỗi xảy ra khi xóa tất cả phim!");
    }
}

// 🟦 Xử lý sự kiện xóa phim yêu thích
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".btn-remove-favorite").forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();

            const userId = this.getAttribute("data-userId");
            const movieId = this.getAttribute("data-movieId");

            removeFavorite(userId, movieId);
        });
    });
});
