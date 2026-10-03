(function () {
  "use strict";

  function buildOption(option, item) {
    var button = document.createElement("button");
    button.type = "button";
    button.className = "mcq-option";
    button.textContent = option.textContent.trim();

    var isCorrect = option.dataset.correct === "true";
    button.dataset.correct = isCorrect ? "true" : "false";
    button.addEventListener("click", function () {
      if (item.dataset.answered === "true") return;
      item.dataset.answered = "true";

      var buttons = item.querySelectorAll(".mcq-option");
      for (var i = 0; i < buttons.length; i++) {
        buttons[i].disabled = true;
        if (buttons[i].dataset.correct === "true") {
          buttons[i].classList.add("mcq-correct");
        }
      }

      if (isCorrect) {
        button.classList.add("mcq-chosen-right");
        state.right += 1;
      } else {
        button.classList.add("mcq-chosen-wrong");
        state.wrong += 1;
      }

      showFeedback(item, isCorrect);
      updateScore();
    });

    return button;
  }

  function showFeedback(item, isCorrect) {
    var why = item.dataset.why || "";
    var box = document.createElement("p");
    box.className = "mcq-feedback " + (isCorrect ? "is-right" : "is-wrong");
    box.textContent = (isCorrect ? "Correct. " : "Not quite. ") + why;
    item.appendChild(box);
  }

  function updateScore() {
    var bar = document.querySelector("[data-quiz-score]");
    if (!bar) return;
    bar.textContent = state.right + " / " + (state.right + state.wrong);
  }

  var state = { right: 0, wrong: 0 };

  function init() {
    var items = document.querySelectorAll(".mcq");
    for (var i = 0; i < items.length; i++) {
      var item = items[i];
      var list = item.querySelector(".mcq-options");
      if (!list) continue;

      var options = list.querySelectorAll("li");
      for (var j = 0; j < options.length; j++) {
        var button = buildOption(options[j], item);
        options[j].innerHTML = "";
        options[j].appendChild(button);
        options[j].classList.add("mcq-option-slot");
      }
      list.classList.add("mcq-options-ready");
    }
    updateScore();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();