function versionChange(value){
    if(value==="0")document.getElementById('versionChangeButton').innerHTML="";
    else document.getElementById('versionChangeButton').innerHTML= `<a type="button" href="/jobportal/student/${studentId}/OAList/${OAId}/${value}" class="btn btn-primary">確認</a>`;
}
window.addEventListener('load',()=>{
    console.log(`${OAId}`);
    console.log(studentId);
})