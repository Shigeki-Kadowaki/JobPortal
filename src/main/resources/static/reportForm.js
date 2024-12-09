window.addEventListener('DOMContentLoaded', () => {
    let prevSelected = "firstLoad";
    const formRadio = document.getElementsByName('testSelect');
    const formSections = {
        "AptitudeTest": document.querySelectorAll('.AptitudeTest'),
        "Interview": document.querySelectorAll('.Interview')
    };

    // ラジオボタン変更時の処理
    formRadio.forEach(target => {
        if (target.checked) {
            prevSelected = target.value;
        }

        target.addEventListener('change', () => {
            // 以前のフォームをすべて非表示
            Object.values(formSections).forEach(section => {
                section.forEach(el => el.style.display = 'none');
            });

            // 現在選択されているフォームを表示
            formSections[target.value].forEach(el => el.style.display = 'block');

            // 現在の選択を記録
            prevSelected = target.value;
        });
    });

    // 初期状態の設定
    if (prevSelected === "firstLoad") {
        prevSelected = formRadio[0]?.value || "AptitudeTest";
    }

    // 初期フォームの表示設定
    Object.values(formSections).forEach(section => {
        section.forEach(el => el.style.display = 'none');
    });
    formSections[prevSelected].forEach(el => el.style.display = 'block');
});
