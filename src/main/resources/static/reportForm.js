
window.addEventListener('DOMContentLoaded',()=>{
    if(document.getElementById("add")) {
        const btn = document.getElementById("add");
        btn.addEventListener("click", addElement, false);
    }
});

let cnt = 0;
function addElement() {
    cnt++;
    let company = document.createElement("div");
    company.id = "company" + cnt;
    company.innerHTML = `
        <fieldset>
            <legend>訪問企業`+ cnt + `</legend>
            <p>企業名</p>
            <input type="text" name="companyName">
            <p>担当者名</p>
            <input type="text" name="manager">
            <p>業種</p>
            <input type="text" name="industry">
            <p>感想</p>
            <input type="text" name="seminarImpressions">
            <p>今後の受験はどうしますか？</p>
            <div id="isSelection">
                <input type="radio" name="isSelection[` + cnt +`]" value="しない" checked onclick="isSelectionsChoice()"> しない
                <input type="radio" name="isSelection[` + cnt +`]" value="検討中" onclick="isSelectionsChoice()"> 検討中
                <input type="radio" name="isSelection[` + cnt +`]" value="受験する" onclick="isSelectionsChoice()"> 受験する
                <input type="radio" name="isSelection[` + cnt +`]" value="内定済み" onclick="isSelectionsChoice()"> 内定済み
            </div>
           <div id="nextAction`+ cnt + `" style="display:none">
                <input type="radio" name="nextAction[` + cnt + `]" checked> 書類郵送
                <input type="radio" name="nextAction[` + cnt + `]"> 説明会参加
                <input type="radio" name="nextAction[` + cnt + `]"> 面接
                <input type="radio" name="nextAction[` + cnt + `]"> 学科試験
                <input type="radio" name="nextAction[` + cnt + `]"> 適性検査
           </div>
        </fieldset>
    `;

    const parent = document.getElementById("visitedCompany");
    parent.appendChild(company);
}

function isSelectionsChoice(){
    if(cnt != 0){
    for (let i = 1; i <= cnt; i++){
            let isSelection = document.getElementsByName(String('isSelection[' + i + ']'));
                console.log(isSelection);
                if(isSelection[2].checked == true){
                    document.getElementById(String("nextAction"+[i])).style.display="";
                }else{
                    document.getElementById(String("nextAction"+[i])).style.display="none";
                }
            }
    }else {
            if(isSelection[2].checked == true){
                document.getElementById("nextAction").style.display="";
            }else{
                document.getElementsByName("nextAction").value="NULL";
                document.getElementById("nextAction").style.display="none";
                }
    }
}