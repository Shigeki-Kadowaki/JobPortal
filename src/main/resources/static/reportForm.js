
window.addEventListener('DOMContentLoaded',()=>{
    document.getElementById('firstCheck').checked = true;
    if(document.getElementById('takingExam').checked === true) visibleNextAction();
});

let cnt = 0;
function addElement() {
    let company = document.createElement('div');
    company.id = 'company' + cnt;
    company.className='card';
    let companyHeader = document.createElement('div');
    companyHeader.className = 'card-header';
    companyHeader.innerHTML = `<div class="d-flex justify-content-between">訪問企業${cnt + 1}
                                    <div class="text-end">
                                        <button type="button" id="removeButton${cnt}" class="btn btn-danger" onclick="removeCompany(${cnt})">削除</button>
                                    </div>
                               </div>`;
    let companyBody = document.createElement('div');
    companyBody.className = 'card-body';
    companyBody.innerHTML = `
        <div class="form-group">
            <p>企業名</p>
            <input class="form-control" type='text' name="seminarForms[${cnt}].companyName" th:value="\${ReportForm.seminarForms[${cnt}].companyName}">
        </div>
        <div class="form-group">
            <p>担当者名</p>
            <input class="form-control" type='text' name="seminarForms[${cnt}].manager" th:value="\${ReportForm.seminarForms[${cnt}].manager}">
        </div>
        <div class="form-group">
            <p>業種</p>
            <input class="form-control" type='text' name="seminarForms[${cnt}].industry" th:value="\${ReportForm.seminarForms[${cnt}].industry}">
        </div>
        <div class="form-group">
            <p>感想</p>
            <input class="form-control" type='text' name="seminarForms[${cnt}].seminarImpressions" th:value="\${ReportForm.seminarForms[${cnt}].seminarImpressions}">
        </div>
        <p class="mt-3">今後の受験はどうしますか？</p>
        <div id='isSelection${cnt}'>
            <input type='radio' name="seminarForms[${cnt}].seminarEmploymentIntention" value='doNot' checked th:checked="\${ReportForm.seminarForms[${cnt}].seminarEmploymentIntention == 'doNot'}" onclick='hiddenNextAction(${cnt})'> しない
            <input type='radio' name="seminarForms[${cnt}].seminarEmploymentIntention" value='underConsideration' th:checked="\${ReportForm.seminarForms[${cnt}].seminarEmploymentIntention == 'underConsideration'}" onclick='hiddenNextAction(${cnt})'> 検討中
            <input type='radio' name="seminarForms[${cnt}].seminarEmploymentIntention" value='takingExam' th:checked="\${ReportForm.seminarForms[${cnt}].seminarEmploymentIntention == 'takingExam'}" onclick='visibleNextAction(${cnt})'> 受験する
            <input type='radio' name="seminarForms[${cnt}].seminarEmploymentIntention" value='hasOffer' th:checked="\${ReportForm.seminarForms[${cnt}].seminarEmploymentIntention == 'hasOffer'}" onclick='hiddenNextAction(${cnt})'> 内定済み
            <div class="form-group mt-3">
                <input class="form-check-input" type='radio' name="seminarForms[${cnt}].seminarIsSelection" value='doNot' checked th:checked="\${ReportForm.seminarForms[${cnt}].seminarIsSelection == 'doNot'}" onclick='hiddenNextAction(${cnt})'> しない
                <input class="form-check-input" type='radio' name="seminarForms[${cnt}].seminarIsSelection" value='underConsideration' th:checked="\${ReportForm.seminarForms[${cnt}].seminarIsSelection == 'underConsideration'}" onclick='hiddenNextAction(${cnt})'> 検討中
                <input class="form-check-input" type='radio' name="seminarForms[${cnt}].seminarIsSelection" value='takingExam' th:checked="\${ReportForm.seminarForms[${cnt}].seminarIsSelection == 'takingExam'}" onclick='visibleNextAction(${cnt})'> 受験する
                <input class="form-check-input" type='radio' name="seminarForms[${cnt}].seminarIsSelection" value='hasOffer' th:checked="\${ReportForm.seminarForms[${cnt}].seminarIsSelection == 'hasOffer'}" onclick='hiddenNextAction(${cnt})'> 内定済み
            </div>
        </div>
       <div id='nextAction${cnt}' style='display:none'>
            <div class="form-group mt-3">
                <input class="form-check-input" type='radio' name="seminarForms[${cnt}].seminarNextAction" value="mailingDocuments" checked th:checked="\${ReportForm.seminarForms[${cnt}].seminarNextAction == 'mailingDocuments'}"> 書類郵送
                <input class="form-check-input" type='radio' name="seminarForms[${cnt}].seminarNextAction" value="attendingBriefing" th:checked="\${ReportForm.seminarForms[${cnt}].seminarNextAction == 'attendingBriefing'}"> 説明会参加
                <input class="form-check-input" type='radio' name="seminarForms[${cnt}].seminarNextAction" value="jobInterview" th:checked="\${ReportForm.seminarForms[${cnt}].seminarNextAction == 'jobInterview'}"> 面接
                <input class="form-check-input" type='radio' name="seminarForms[${cnt}].seminarNextAction" value="exam" th:checked="\${ReportForm.seminarForms[${cnt}].seminarNextAction == 'exam'}"> 学科試験
                <input class="form-check-input" type='radio' name="seminarForms[${cnt}].seminarNextAction" value="aptitudeTest" th:checked="\${ReportForm.seminarForms[${cnt}].seminarNextAction == 'aptitudeTest'}"> 適性検査
            </div>
       </div>
    `;
    company.appendChild(companyHeader);
    company.appendChild(companyBody);
    document.getElementById('visitedCompany').appendChild(company);

    document.getElementById(`removeButton${cnt}`).style.display = 'inline';
    if(cnt !== 0){
        document.getElementById(`removeButton${cnt - 1}`).style.display = 'none';
    }
    cnt++;
}

function removeCompany(removeCompany){
    document.getElementById(`company${removeCompany}`).remove();
    cnt--;
    document.getElementById(`removeButton${cnt}`).style.display = 'inline';
}

function hiddenNextAction(count = ""){
    document.getElementById(`nextAction${count}`).style.display = 'none';
}

function visibleNextAction(count = ""){
    document.getElementById(`nextAction${count}`).style.display = 'inline';
}

// function isSelectionsChoice(){
//     if(cnt !== 0){
//     for (let i = 1; i <= cnt; i++){
//             let isSelection = document.getElementsByName(String('seminarEmploymentIntention' + i));
//                 if(isSelection[2].checked === true){
//                     document.getElementById(String('nextAction' + i)).style.display='';
//                 }else{
//                     document.getElementById(String('nextAction' + i)).style.display='none';
//                 }
//             }
//     }else {
//     let isSelection = document.getElementsByName('isSelection');
//                 if(isSelection[2].checked === true){
//                     document.getElementById('nextAction').style.display='';
//                 }else{
//                     document.getElementsByName('nextAction').value='';
//                     document.getElementById('nextAction').style.display='none';
//                 }
//     }
// }