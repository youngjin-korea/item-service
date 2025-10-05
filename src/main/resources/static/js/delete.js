document.addEventListener("DOMContentLoaded", function () {

    const deleteBtn = document.getElementById("deleteBtn");

    deleteBtn.addEventListener("click", function () {
           const itemId = deleteBtn.getAttribute("data-id"); // 삭제할 itemId 가져오기

                if (!itemId) {
                    return;
                }

                if (!confirm("정말 삭제하시겠습니까?")) {
                    return;
                }

                fetch(`/basic/items/${itemId}/edit`, {
                    method: "DELETE"
                })
                .then(response => {
                    if (response.ok) {
                        alert("삭제되었습니다.");
                        window.location.href = "/basic/items"; // 목록 페이지로 이동
                    } else {
                        alert("삭제 실패!");
                    }
                })
                .catch(error => {
                    console.error("삭제 중 오류 발생:", error);
                    alert("서버 오류가 발생했습니다.");
                });
    })
})