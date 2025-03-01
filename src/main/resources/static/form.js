const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));

const subjectMap = new Map();
const exceptionMap = new Map();
function addOADate(selectedDate, selectedPeriods = null) {
    if (document.getElementById('OATime_' + selectedDate)) {
        return;
    }
    let formatedDate = formatDate(selectedDate, "/") ;
    let OADate=document.createElement('div');
    OADate.id="OATime_"+selectedDate;
    OADate.className="card";
    let OADateHeader=document.createElement('div');
    OADateHeader.className="card-header";

    let strToday = formatDate(selectedDate, "-");
    let dateToday = new Date(strToday);
    let weekdayNumber;
    if(exceptionMap.has(strToday)){
        console.log("exception!")
        weekdayNumber = exceptionMap.get(strToday) - 1;
    }else{
        console.log("normal")
        weekdayNumber = dateToday.getDay() - 1;
    }
    if(weekdayNumber === -1 || weekdayNumber === 5) return false;
    OADateHeader.innerHTML=`<div class="d-flex justify-content-between">${formatedDate} の公欠授業を選択
                                <div class="text-end">
                                    <button type="button" class="btn btn-danger" onclick="removeOADate(${selectedDate})">削除</button>
                                </div>
                            </div>`;
    let OADateBody=document.createElement('div');
    OADateBody.className="card-body";
    let text=`<input type="hidden" name="OADates[]" value="${selectedDate}">`;
    let index = 1;
    for(let subject of subjects[weekdayNumber]){
        if(subject["id"] !== -1){
            text+=`
                <input type="checkbox" class="btn-check" id="${selectedDate}${index}period" name="OAPeriods[${selectedDate}][]" value="${index}" onchange="toggleDateCheck(${selectedDate}, ${index})" autocomplete="off">
                <label class="btn btn-outline-primary" for="${selectedDate}${index}period">${index}限目 : ${subject["name"]}</label>
                <input type="checkbox" class="btn-check" id="${selectedDate}${index}name" name="OASubjects[${selectedDate}][]" value="${subject["name"]}" autocomplete="off">
                <label class="btn btn-outline-primary" for="${selectedDate}${index}name" style="display: none;">${subject["name"]}</label>
            `;
        }
        index++;
    }
    OADateBody.innerHTML=text;
    OADate.appendChild(OADateHeader);
    OADate.appendChild(OADateBody);
    document.getElementById('OADates').appendChild(OADate);

    if(selectedPeriods !== null){
        selectedPeriods.forEach(e=>{
            document.getElementById(`${selectedDate}${e}period`).checked = true;
            document.getElementById(`${selectedDate}${e}name`).checked = true;
        });
    }
 }

function toggleDateCheck(date, period){
    document.getElementById(`${date}${period}name`).checked = document.getElementById(`${date}${period}period`).checked;
}

function removeOADate(removeOADate){
    document.getElementById("OATime_" + removeOADate).remove();
}

function formatDate(date, sep="") {
  const yyyy = date.substring(0,4);
  const mm = date.substring(4,6);
  const dd = date.substring(6,8);

  return `${yyyy}${sep}${mm}${sep}${dd}`;
}

let prevSelected = "firstLoad";
window.addEventListener('DOMContentLoaded',()=>{
    //公欠理由によってフォームを変える
    let formRadio = document.getElementsByName('reasonForAbsence');
    formRadio.forEach((target)=>{
        target.addEventListener('change',()=>{
          document.getElementsByClassName(prevSelected)[0].style.display='none';
          document.getElementsByClassName(target.value)[0].style.display='block';
          document.getElementsByClassName(prevSelected)[1].style.display='none';
          document.getElementsByClassName(target.value)[1].style.display='block';

          prevSelected=target.value;
        });
        if(target.checked){
        prevSelected=target.value;
        }
    });
    if(prevSelected==="firstLoad"){
        prevSelected=document.getElementsByName("reasonForAbsence")[0].value;
    }
    document.getElementsByClassName('jobSearch')[0].style.display='none';
    document.getElementsByClassName('seminar')[0].style.display='none';
    document.getElementsByClassName('bereavement')[0].style.display='none';
    document.getElementsByClassName('attendanceBan')[0].style.display='none';
    document.getElementsByClassName('other')[0].style.display='none';
    document.getElementsByClassName(prevSelected)[0].style.display='block';
    document.getElementsByClassName('jobSearch')[1].style.display='none';
    document.getElementsByClassName('seminar')[1].style.display='none';
    document.getElementsByClassName('bereavement')[1].style.display='none';
    document.getElementsByClassName('attendanceBan')[1].style.display='none';
    document.getElementsByClassName('other')[1].style.display='none';
    document.getElementsByClassName(prevSelected)[1].style.display='block';

    for (let exceptionDate of exceptionDates){
        exceptionMap.set(exceptionDate.exceptionDate, exceptionDate.weekdayNumber);
    }

    document.getElementById('jobSearch').checked = true;
})
