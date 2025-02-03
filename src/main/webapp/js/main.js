// 토글 기능 & 현재 시간 표시

function toggleSidebar() {
  const sidebar = document.getElementById("sidebar");
  sidebar.classList.toggle("active");
  adjustMainSize();
}

function checkScreenSize() {
  const sidebar = document.getElementById("sidebar");
  if (window.innerWidth > 768) {
    sidebar.classList.remove("active"); // 데스크탑에서는 항상 보이게
  }
}

window.addEventListener("resize", checkScreenSize);

checkScreenSize();

function updateTime() {
  const now = new Date();
  const formattedTime = now.toLocaleTimeString("ko-KR", {
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  });
  document.getElementById("current-time").textContent = formattedTime;
}

setInterval(updateTime, 1000); //1초마다 시간 업데이트
updateTime(); // 페이지 로드 시 바로 실행

//유저 아이콘 드롭다운

function toggleUserMenu() {
  document.getElementById("user-menu").classList.toggle("active");
}

document.addEventListener("click", function (event) {
  const dropdown = document.getElementById("user-menu");
  const button = document.querySelector(".user-icon");

  if (!button.contains(event.target) && !dropdown.contains(event.target)) {
    dropdown.classList.remove("active");
  }
});

// 🟢 AJAX로 페이지 로드 (URL 변경 포함)
function loadPage(page, addToHistory = true) {
  fetch(page)
    .then((response) => response.text())
    .then((data) => {
      document.getElementById("main-content").innerHTML = data;
      adjustMainSize(); // 사이드바 상태에 맞춰 main 크기 조정
      loadCSS(page); // 페이지에 맞는 CSS 추가

      // 🟢 현재 페이지를 localStorage에 저장
      localStorage.setItem("currentPage", page);

      // 🟢 URL 변경 & 히스토리 추가 (앞으로 가기 문제 해결)
      if (addToHistory) {
        history.pushState({ page: page }, "", "?page=" + page);
      }
    })
    .catch((error) => console.error("Error loading page:", error));
}

// 🟢 뒤로 가기 & 앞으로 가기 버튼 클릭 시 `main-content`만 업데이트
window.addEventListener("popstate", function (event) {
  if (event.state && event.state.page) {
    loadPage(event.state.page, false); // `pushState` 실행하지 않고 컨텐츠만 업데이트
  } else {
    loadPage("board.html", false); // 기본값 (게시판)
  }
});

// 🟢 페이지 로드 시 URL에 따라 `main-content` 업데이트 (중복 제거)
document.addEventListener("DOMContentLoaded", function () {
  const urlParams = new URLSearchParams(window.location.search);
  const page =
    urlParams.get("page") ||
    localStorage.getItem("currentPage") ||
    "board.html"; // 기본값: 게시판
  loadPage(page, false); // `pushState` 실행하지 않음
});
//메인 크기 조정 -

function adjustMainSize() {
  const sidebar = document.getElementById("sidebar");
  const mainRegion = document.getElementById("main-region");

  if (window.innerWidth > 768) {
    if (sidebar.classList.contains("active")) {
      mainRegion.style.marginLeft = "0";
      mainRegion.style.width = "100%";
    } else {
      mainRegion.style.marginLeft = "250px";
      mainRegion.style.width = "calc(100% - 250px)";
    } //데스크탑 크기에서는 사이드바 active 여부에 맞추어 main 화면 동적 조절
  } else {
    mainRegion.style.marginLeft = "0";
    mainRegion.style.width = "100%";
  } //모바일에서는 사이드바와 겹침 상관없이 main 영역이 화면에 꽉 차도록 조절
}

window.addEventListener("resize", adjustMainSize);
window.addEventListener("load", adjustMainSize);

//main content에 해당하는 html에 css 동적 적용
function loadCSS(page) {
  let cssFile;
  if (page === "board.html") {
    cssFile = "../css/board.css";
  } else if (page == "b_write.html") {
    cssFile = "../css/b_write.css";
  } else if (page == "b_view.html") {
    cssFile = "../css/b_view.css";
  }

  if (cssFile) {
    let link = document.createElement("link");
    link.rel = "stylesheet";
    link.href = cssFile;
    link.id = "dynamic-css";

    // 기존에 추가된 CSS 제거 후 새 CSS 추가
    let existingCSS = document.getElementById("dynamic-css");
    if (existingCSS) {
      existingCSS.remove();
    }
    document.head.appendChild(link);
  }
}
