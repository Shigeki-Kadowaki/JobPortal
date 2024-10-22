function addOADate(selectedDate) {
            if (document.getElementById('OATime_' + selectedDate)) {
                return;
            }

           var OADate=document.createElement('div');
           OADate.id="OATime_"+selectedDate;
           OADate.className="card";
           var OADateHeader=document.createElement('div');
           OADateHeader.className="card-header";
           OADateHeader.innerHTML=`${selectedDate} の公欠授業を選択`;
           var OADateBody=document.createElement('div');
           OADateBody.className="card-body";
           var text=`<input type="hidden" name="selectedDates" value="${selectedDate}">`;
           for (let index = 1; index < 6; index++) {
            text+=`
            <input type="checkbox" class="btn-check" id="${selectedDate}` + index + `" name="periods[${selectedDate}][]" value="` + index + `" autocomplete="off">
            <label class="btn btn-outline-primary" for="${selectedDate}` + index + `">` + index + `限目</label>
            `;
           }
           OADateBody.innerHTML=text;
           OADate.appendChild(OADateHeader);
           OADate.appendChild(OADateBody);
           document.getElementById('OADates').appendChild(OADate);
 }