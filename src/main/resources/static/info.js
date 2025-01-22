// function versionChange(value){
//     if(value==="0")document.getElementById('versionChangeButton').innerHTML="";
//     else document.getElementById('versionChangeButton').innerHTML= `<a type="button" href="/jobportal/student/${studentId}/OAList/${OAId}/${value}" class="btn btn-primary">確認</a>`;
// }

window.addEventListener('load',()=>{
})
function versionChange(value){
    if(value === "0"){
        fetch(`/jobportal/student/${studentId}/OAList/${OAId}`)
            .then(response =>{
                if(!response.ok){
                    throw new Error('Network response was not ok 0');
                }
                return response.text();
            })
            .then(html =>{
                document.getElementById('container').innerHTML=html;
            })
            .catch(error => console.error('Error:', error))
    }else(
        fetch(`/jobportal/student/${studentId}/${OAId}?version=${value}`)
            .then(response =>{
                if(!response.ok){
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(html =>{
                document.getElementById('container').innerHTML=html;
                document.getElementById('versionSelect').options[parseInt(maxVersion)-parseInt(value)].selected = true;
            })
            .catch(error => console.error('Error:', error))
    );
}
function reportVersionChange(value){
    if(value === "0"){
        fetch(`/jobportal/student/${studentId}/report/${reportId}`)
            .then(response =>{
                if(!response.ok){
                    throw new Error('Network response was not ok 0');
                }
                return response.text();
            })
            .then(html =>{
                document.getElementById('container').innerHTML=html;
            })
            .catch(error => console.error('Error:', error))
    }else(
        fetch(`/jobportal/student/${studentId}/reportByVersion/${reportId}?version=${value}`)
            .then(response =>{
                if(!response.ok){
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(html =>{
                document.getElementById('container').innerHTML=html;
                document.getElementById('versionSelect').options[parseInt(maxVersion)-parseInt(value)].selected = true;
            })
            .catch(error => console.error('Error:', error))
    );
}