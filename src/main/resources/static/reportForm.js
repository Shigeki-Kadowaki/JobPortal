function testSelect() {
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

window.addEventListener('load', function {
const testSelect = document.getElementsByName("testSelect")
const btn = document.getElementById("add");
testSelect.addEventListener("click",testSelect, false)
btn.addEventListener("click", addElement, false);
})

  function addElement() {
            cnt++;
            let company = document.createElement("div");
            company.id = "company" + cnt;
            company.innerHTML = `
            <fieldset>
                <legend>訪問企業`+ cnt + `</legend>
                <p>企業名</p>
                <input type="text">
                <p>担当者名</p>
                <input type="text">
                <p>業種</p>
                <input type="text">
                <p>感想</p>
                <input type="text">
                <p>今後の受験はどうしますか？</p>
                <div id="isSelection` + cnt + `">
                    <input type="radio" name="isSelection` + cnt +`" value="しない" checked onclick="isSelectionsChoice()"> しない
                    <input type="radio" name="isSelection` + cnt +`" value="検討中" onclick="isSelectionsChoice()"> 検討中
                    <input type="radio" name="isSelection` + cnt +`" value="受験する" onclick="isSelectionsChoice()"> 受験する
                </div>
                <div id="nextAction` + cnt + `">
                    <input type="radio" name="nextAction` + cnt + `" checked> 書類郵送
                    <input type="radio" name="nextAction` + cnt + `"> 説明会参加
                    <input type="radio" name="nextAction` + cnt + `"> その他
                    <textarea rows="1" cols="40"></textarea>
                </div>
            </fieldset>
            `;

            const parent = document.getElementById("visitedCompany");
            parent.appendChild(company);
        }