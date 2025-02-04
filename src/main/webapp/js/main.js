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

function loadPage(page) {
  fetch(page)
    .then((response) => response.text())
    .then((data) => {
      let container = document.getElementById("main-content");
      container.innerHTML = data; // HTML 삽입

      adjustMainSize(); // 사이드바 상태에 맞춰 main 크기 조정
      loadCSS(page); // CSS 로드 함수 실행

      executeScripts(container); // 새로 불러온 HTML 내 스크립트 실행
    })
    .catch((error) => console.error("페이지 로드 오류:", error));
}

// 🔽 내부 <script> 태그 실행 함수
function executeScripts(container) {
  let scripts = container.getElementsByTagName("script");

  for (let oldScript of scripts) {
    let newScript = document.createElement("script");
    newScript.text = oldScript.innerText; // 내부 스크립트 복사
    document.body.appendChild(newScript); // 실행을 위해 body에 추가
    oldScript.remove(); // 기존 <script> 제거 (중복 실행 방지)
  }
}

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