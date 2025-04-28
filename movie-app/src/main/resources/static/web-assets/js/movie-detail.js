
// Hàm format ngày theo định dạng dd/MM/yyyy
const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    let day = date.getDate();
    let month = date.getMonth() + 1;
    let year = date.getFullYear();
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;
    return `${day}/${month}/${year}`;
};

// Hàm render danh sách reviews sử dụng template string
const renderReviews = (reviews) => {
    const html = reviews.map(review => `
        <div class="review-item">
            <div class="review-author d-flex justify-content-between">
                <div class="d-flex">
                    <div class="review-author-avatar">
                        <img src="${review.user.avatar || 'https://homepage.momocdn.net/cinema/momo-cdn-api-220615142913-637909001532744942.jpg'}" alt="">
                    </div>
                    <div class="ms-3">
                        <p class="fw-semibold">${review.user.displayName}</p>
                        <p>⭐️ ${review.rating}/10</p>
                    </div>
                </div>
                <div>
                    <p>${formatDate(review.createdAt)}</p>
                </div>
            </div>
            <div class="review-content mt-3">
                <p>${review.content}</p>
            </div>
            <div class="review-actions mt-2">
                <button class="btn btn-sm btn-primary">Sửa</button>
                <button class="btn btn-sm btn-danger" onclick="deleteReview(${review.id})">Xóa</button>
            </div>
        </div>
    `).join('');
    document.querySelector('.review-list').innerHTML = html;
};

// Hàm render phân trang sử dụng template string
const renderPagination = (totalPages, currentPage) => {
    let paginationHTML = `
        <ul class="pagination justify-content-center">
            ${currentPage <= 1
        ? `<li class="page-item disabled"><button class="page-link" disabled>Previous</button></li>`
        : `<li class="page-item"><button class="page-link" onclick="getReviews(${currentPage - 1})">Previous</button></li>`
    }
    `;

    for (let i = 1; i <= totalPages; i++) {
        paginationHTML += i === currentPage
            ? `<li class="page-item active"><button class="page-link" onclick="getReviews(${i})">${i}</button></li>`
            : `<li class="page-item"><button class="page-link" onclick="getReviews(${i})">${i}</button></li>`;
    }

    paginationHTML += `
            ${currentPage >= totalPages
        ? `<li class="page-item disabled"><button class="page-link" disabled>Next</button></li>`
        : `<li class="page-item"><button class="page-link" onclick="getReviews(${currentPage + 1})">Next</button></li>`
    }
        </ul>
    `;
    document.querySelector('nav.mt-4').innerHTML = paginationHTML;
};


// Hàm lấy reviews (sử dụng async/await, arrow function và try/catch)
const getReviews = async (page) => {
    try {
        const movieId = movie.id;
        const response = await axios.get('/api/reviews', {
            params: {
                movieId: movieId,
                page: page,
                pageSize: 5
            }
        });

        // Giả sử API trả về đối tượng có cấu trúc:
        // { content: [...reviews], totalPages: number, number: currentPage (0-indexed) }
        const reviewPage = response.data;
        renderReviews(reviewPage.content);
        renderPagination(reviewPage.totalPages, reviewPage.number + 1);
    } catch (error) {
        console.log(error);
    }
};

// Hàm xóa review sử dụng async/await và window.confirm
const deleteReview = async (id) => {
    if (!window.confirm("Bạn có chắc chắn muốn xóa review này không?")) {
        return;
    }
    try {
        await axios.delete(`/api/reviews/${id}`);
        // Nếu cần cập nhật lại danh sách review cho trang 1, có thể gọi:
        getReviews(1);
    } catch (error) {
        console.log(error);
    }
};

/* ---- Xử lý tạo review ---- */
const reviewForm = document.getElementById("review-form");
const reviewContentEl = document.getElementById("review-content");

// Sử dụng biến global cho rating đã chọn (được cập nhật từ phần chọn rating)
let currentRating = 0;

// Xử lý chọn rating (code hiện tại đã có)
const stars = document.querySelectorAll(".star");
const ratingValue = document.getElementById("rating-value");

stars.forEach((star) => {
    star.addEventListener("mouseover", () => {
        resetStars();
        const rating = parseInt(star.getAttribute("data-rating"));
        highlightStars(rating);
    });

    star.addEventListener("mouseout", () => {
        resetStars();
        highlightStars(currentRating);
    });

    star.addEventListener("click", () => {
        currentRating = parseInt(star.getAttribute("data-rating"));
        ratingValue.textContent = `Bạn đã đánh giá ${currentRating} sao.`;
        highlightStars(currentRating);
    });
});

function resetStars() {
    stars.forEach((star) => {
        star.classList.remove("active");
    });
}

function highlightStars(rating) {
    stars.forEach((star) => {
        const starRating = parseInt(star.getAttribute("data-rating"));
        if (starRating <= rating) {
            star.classList.add("active");
        }
    });
}

// Xử lý submit form tạo review
reviewForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    // Validate rating
    if (currentRating === 0) {
        alert("Vui lòng chọn rating");
        return;
    }

    // Validate content
    const content = reviewContentEl.value.trim();
    if (!content) {
        alert("Vui lòng nhập nội dung");
        return;
    }

    // Tạo payload cho API
    const payload = {
        content: content,
        rating: currentRating,
        movieId: movie.id
    };

    try {
        await axios.post('/api/reviews', payload);
        alert("Tạo review thành công");

        // Đóng modal sử dụng Bootstrap 5
        const modalEl = document.getElementById('modal-review');
        const modalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
        modalInstance.hide();

        // Reset form và UI rating
        reviewForm.reset();
        currentRating = 0;
        resetStars();
        ratingValue.textContent = "";

        // Nếu muốn, có thể cập nhật lại danh sách review ngay sau khi tạo
        getReviews(1);
    } catch (error) {
        console.log(error);
    }
});


// Điền dữ liệu vào modal chỉnh sửa
    document.querySelectorAll('.edit-review').forEach(button => {
        button.addEventListener('click', () => {
            document.getElementById('edit-review-id').value = button.getAttribute('data-review-id');
            document.getElementById('edit-review-content').value = button.getAttribute('data-review-content');
            document.getElementById('edit-review-rating').value = button.getAttribute('data-review-rating');
        });
    });

    // Xóa review
    document.querySelectorAll('.delete-review').forEach(button => {
        button.addEventListener('click', () => {
            const reviewId = button.getAttribute('data-review-id');
            if (confirm('Bạn có chắc muốn xóa bình luận này?')) {
                fetch(`/api/reviews/${reviewId}`, {
                    method: 'DELETE'
                }).then(response => {
                    if (response.ok) {
                        location.reload();
                    } else {
                        alert('Lỗi khi xóa bình luận');
                    }
                });
            }
        });
    });

    // Cập nhật review
    document.getElementById('edit-review-form').addEventListener('submit', event => {
        event.preventDefault();
        const reviewId = document.getElementById('edit-review-id').value;
        const content = document.getElementById('edit-review-content').value;
        const rating = document.getElementById('edit-review-rating').value;

        fetch(`/api/reviews/${reviewId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ content, rating })
        }).then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Lỗi khi cập nhật bình luận');
            }
        });
    });


document.addEventListener("DOMContentLoaded", function () {
        const reviewForm = document.getElementById("review-form");
        const reviewIdInput = document.getElementById("edit-review-id");
        const reviewContentInput = document.getElementById("edit-review-content");
        const reviewRatingInput = document.getElementById("edit-review-rating");
        const modalTitle = document.getElementById("exampleModalLabel");

        // Điền dữ liệu vào modal chỉnh sửa
        document.querySelectorAll(".edit-review").forEach(button => {
            button.addEventListener("click", () => {
                modalTitle.textContent = "Chỉnh sửa bình luận";
                reviewIdInput.value = button.getAttribute("data-review-id") || "";
                reviewContentInput.value = button.getAttribute("data-review-content") || "";
                reviewRatingInput.value = button.getAttribute("data-review-rating") || "";
            });
        });

        // Xóa review có kiểm tra quyền
        document.querySelectorAll(".delete-review").forEach(button => {
            button.addEventListener("click", () => {
                const reviewId = button.getAttribute("data-review-id");
                if (confirm("Bạn có chắc muốn xóa bình luận này?")) {
                    fetch(`/api/reviews/${reviewId}?userId=1`, { // 🔹 Thay userId bằng ID người dùng thực tế
                        method: "DELETE"
                    }).then(response => {
                        if (response.ok) {
                            location.reload();
                        } else {
                            alert("Bạn không có quyền xóa bình luận này");
                        }
                    });
                }
            });
        });

        // Cập nhật review với thông báo phản hồi
        document.getElementById("edit-review-form").addEventListener("submit", event => {
            event.preventDefault();
            const reviewId = reviewIdInput.value;
            const content = reviewContentInput.value;
            const rating = reviewRatingInput.value;

            fetch(`/api/reviews/${reviewId}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ content, rating })
            }).then(response => {
                if (response.ok) {
                    alert("Cập nhật bình luận thành công!");
                    location.reload();
                } else {
                    alert("Lỗi khi cập nhật bình luận. Vui lòng thử lại.");
                }
            });
        });
    });
// Khởi chạy load trang đầu tiên
getReviews(1);
