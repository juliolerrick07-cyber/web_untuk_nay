
const menuToggle = document.getElementById("menuToggle");
const navLinks = document.getElementById("navLinks");

// MENU RESPONSIF
menuToggle.addEventListener("click", () => {
  const isOpen = navLinks.classList.toggle("open");
  menuToggle.setAttribute("aria-expanded", String(isOpen));
  menuToggle.setAttribute(
    "aria-label",
    isOpen ? "Tutup menu" : "Buka menu"
  );
});

navLinks.querySelectorAll("a").forEach(link => {
  link.addEventListener("click", () => {
    navLinks.classList.remove("open");
    menuToggle.setAttribute("aria-expanded", "false");
    menuToggle.setAttribute("aria-label", "Buka menu");
  });
});

// FILTER FOTO DAN VIDEO
const filterButtons = document.querySelectorAll(".filter-btn");
const memoryCards = document.querySelectorAll(".memory-card");

filterButtons.forEach(button => {
  button.addEventListener("click", () => {
    const filter = button.dataset.filter;

    filterButtons.forEach(item => {
      item.classList.toggle("active", item === button);
    });

    memoryCards.forEach(card => {
      card.hidden =
        filter !== "all" && card.dataset.type !== filter;
    });
  });
});

// MODAL FOTO DAN VIDEO

const mediaModal = document.getElementById("mediaModal");
const modalContent = document.getElementById("modalContent");
const modalCaption = document.getElementById("modalCaption");
const modalClose = document.getElementById("modalClose");

let lastFocusedElement = null;
let currentImages = [];
let currentImageIndex = 0;
let currentCaption = "";

document.querySelectorAll(".media-open").forEach(button => {
  button.addEventListener("click", () => {
    const kind = button.dataset.kind;
    const caption = button.dataset.caption || "Kenangan kita ❤️";

    lastFocusedElement = button;
    currentCaption = caption;
    modalContent.replaceChildren();

    if (kind === "video") {
      const video = document.createElement("video");
      video.src = button.dataset.src;
      video.controls = true;
      video.autoplay = true;
      video.playsInline = true;
      video.setAttribute("aria-label", caption);
      modalContent.appendChild(video);
    } else {
      try {
        currentImages = button.dataset.images
          ? JSON.parse(button.dataset.images)
          : [button.dataset.src];

        currentImages = currentImages.filter(Boolean);
      } catch (error) {
        currentImages = [button.dataset.src].filter(Boolean);
      }

      currentImageIndex = 0;

      if (currentImages.length === 0) {
        modalContent.textContent = "Foto kenangan belum tersedia.";
      } else {
        const image = document.createElement("img");
        image.className = "modal-photo";
        image.alt = caption;

        const controls = document.createElement("div");
        controls.className = "photo-controls";

        const prevButton = document.createElement("button");
        prevButton.type = "button";
        prevButton.textContent = "← Sebelumnya";
        prevButton.className = "photo-nav";

        const counter = document.createElement("span");
        counter.className = "photo-counter";

        const nextButton = document.createElement("button");
        nextButton.type = "button";
        nextButton.textContent = "Berikutnya →";
        nextButton.className = "photo-nav";

        function showImage() {
          image.src = currentImages[currentImageIndex];
          counter.textContent =
            `${currentImageIndex + 1} / ${currentImages.length}`;

          prevButton.disabled = currentImages.length <= 1;
          nextButton.disabled = currentImages.length <= 1;
        }

        prevButton.addEventListener("click", () => {
          currentImageIndex =
            (currentImageIndex - 1 + currentImages.length) %
            currentImages.length;
          showImage();
        });

        nextButton.addEventListener("click", () => {
          currentImageIndex =
            (currentImageIndex + 1) % currentImages.length;
          showImage();
        });

        controls.append(prevButton, counter, nextButton);
        modalContent.append(image, controls);
        showImage();
      }
    }

    modalCaption.textContent = caption;
    mediaModal.hidden = false;
    document.body.style.overflow = "hidden";
    modalClose.focus();
  });
});

function closeModal() {
  if (mediaModal.hidden) return;

  const video = modalContent.querySelector("video");

  if (video) {
    video.pause();
    video.removeAttribute("src");
    video.load();
  }

  mediaModal.hidden = true;
  modalContent.replaceChildren();
  document.body.style.overflow = "";
  lastFocusedElement?.focus();
}

modalClose.addEventListener("click", closeModal);

mediaModal.addEventListener("click", event => {
  if (event.target === mediaModal) closeModal();
});

document.addEventListener("keydown", event => {
  if (event.key === "Escape") closeModal();
});
// MUSIK LATAR
const backgroundMusic = document.getElementById("backgroundMusic");
const musicToggle = document.getElementById("musicToggle");
const musicStatus = document.getElementById("musicStatus");

musicToggle.addEventListener("click", async () => {
  if (backgroundMusic.paused) {
    try {
      await backgroundMusic.play();
      musicToggle.textContent = "Ⅱ Jeda Musik";
      musicStatus.textContent = "Musik sedang diputar ♡";
    } catch (error) {
      musicStatus.textContent =
        "Musik belum dapat diputar. Periksa file lagu.mp3.";
    }
  } else {
    backgroundMusic.pause();
    musicToggle.textContent = "♫ Putar Musik";
    musicStatus.textContent = "Musik dijeda.";
  }
});

// TOMBOL KEJUTAN
const surpriseBtn = document.getElementById("surpriseBtn");
const surpriseMessage = document.getElementById("surpriseMessage");

surpriseBtn.addEventListener("click", () => {
  surpriseMessage.hidden = false;
  surpriseBtn.textContent = "Kamu Sudah Tahu Rahasiaku ♡";
  surpriseBtn.disabled = true;
});