function addOADate(selectedDate) {

            if (document.getElementById('OATime_' + selectedDate)) {
                return;
            }
           let formatedDate = formatDate(selectedDate, "/") ;
           let OADate=document.createElement('div');
           OADate.id="OATime_"+selectedDate;
           OADate.className="card";
           let OADateHeader=document.createElement('div');
           OADateHeader.className="card-header";
           OADateHeader.innerHTML=`<div class="d-flex justify-content-between">${formatedDate} の公欠授業を選択<div class="text-end">
                                                                           <button type="button" class="btn btn-danger" onclick="removeOADate(${selectedDate})">削除</button>
                                                                       </div></div>`;
           let OADateBody=document.createElement('div');
           console.log(selectedDate);
           OADateBody.className="card-body";
           let text=`<input type="hidden" name="OADates[]" value="${selectedDate}">`;
           for (let index = 1; index < 6; index++) {
            text+=`
            <input type="checkbox" class="btn-check" id="${selectedDate}` + index + `" name="OAPeriods[${selectedDate}][]" value="` + index + `" autocomplete="off">
            <label class="btn btn-outline-primary" for="${selectedDate}` + index + `">` + index + `限目</label>
            `;
           }
           OADateBody.innerHTML=text;
           OADate.appendChild(OADateHeader);
           OADate.appendChild(OADateBody);
           document.getElementById('OADates').appendChild(OADate);
 }

function removeOADate(removeOADate){
    console.log(removeOADate);
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
    let formRadio = document.getElementsByName('reasonForAbsence');
//    let radioCheckID = document.getElementById('reasonAbsence');
    formRadio.forEach((target)=>{
        target.addEventListener('change',()=>{
          console.log(prevSelected);
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
        document.getElementById("jobSearch").checked = true;
        prevSelected="jobSearchForm";
    }
//    document.getElementById('jobSearchForm').style.display='none';
//    document.getElementById('seminarForm').style.display='none';
//    document.getElementById('bereavementForm').style.display='none';
//    document.getElementById('attendanceBanForm').style.display='none';
//    document.getElementById('otherForm').style.display='none';
    document.getElementsByClassName('jobSearchForm')[0].style.display='none';
    document.getElementsByClassName('seminarForm')[0].style.display='none';
    document.getElementsByClassName('bereavementForm')[0].style.display='none';
    document.getElementsByClassName('attendanceBanForm')[0].style.display='none';
    document.getElementsByClassName('otherForm')[0].style.display='none';
    document.getElementsByClassName(prevSelected)[0].style.display='block';
    document.getElementsByClassName('jobSearchForm')[1].style.display='none';
    document.getElementsByClassName('seminarForm')[1].style.display='none';
    document.getElementsByClassName('bereavementForm')[1].style.display='none';
    document.getElementsByClassName('attendanceBanForm')[1].style.display='none';
    document.getElementsByClassName('otherForm')[1].style.display='none';
    document.getElementsByClassName(prevSelected)[1].style.display='block';
    console.log(prevSelected);
    console.log("load");
})
