
window.addEventListener('DOMContentLoaded',()=>{
    if(document.getElementById("add")){
    const btn = document.getElementById("add");
    btn.addEventListener("click", addElement, false);
    }
    if(document.getElementById("TestSelect") || document.getElementById("visited")){
    testSelect();
    }
});

let prevSelected = "firstLoad";
function testSelect() {

    const formRadio = document.getElementsByName('radioSelect');
    const formSections = {
        "cur": document.querySelectorAll('.cur'),
        "prev": document.querySelectorAll('.prev')
    };

    // ラジオボタン変更時の処理
    Array.from(formRadio).forEach(target => {
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
        prevSelected = formRadio[0]?.value || "cur";
    }

    // 初期フォームの表示設定
    Object.values(formSections).forEach(section => {
        section.forEach(el => el.style.display = 'none');
    });
    formSections[prevSelected].forEach(el => el.style.display = 'block');
}

let cnt = 0;
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
                <input type="radio" name="isSelection` + cnt +`" value="内定済み" onclick="isSelectionsChoice()"> 内定済み
            </div>
           <div id="nextAction` + cnt + `" style="display:none">
                <input type="radio" name="nextAction` + cnt + `" checked> 書類郵送
                <input type="radio" name="nextAction` + cnt + `"> 説明会参加
                <input type="radio" name="nextAction` + cnt + `"> 面接
                <input type="radio" name="nextAction` + cnt + `"> 学科試験
                <input type="radio" name="nextAction` + cnt + `"> 適性検査
           </div>
        </fieldset>
    `;

    const parent = document.getElementById("visitedCompany");
    parent.appendChild(company);
}

function isSelectionsChoice(){
    if(cnt != 0){
    for (let i = 1; i <= cnt; i++){
            let isSelection = document.getElementsByName(String('isSelection' + i));
                if(isSelection[2].checked == true){
                    document.getElementById(String("nextAction" + i)).style.display="";
                }else{
                    document.getElementById(String("nextAction" + i)).style.display="none";
                }
            }
    }else {
    let isSelection = document.getElementsByName('isSelection');
                if(isSelection[2].checked == true){
                    document.getElementById("nextAction").style.display="";
                }else{
                    document.getElementById("nextAction").style.display="none";
                }
    }
}