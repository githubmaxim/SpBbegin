//Для всех кнопок на html-листе используем вариант написание через "<script type="text/javascript">$( document ).ready(function(){ $( "button" ).click(function(){---}; </script>".
//Возможно написание привязки функций к кнопкам через "<button onclick=" в файле *.html., но при таком написании функции которые принимают с html-страницы данные выполняются на сервере с первого раза, а клиент получает ответ только со второго раза (а дальше нормально). При этом сервер сработает у себя все разы.

//Чтобы в броузере "Chrome" в "localhost:8080" увидеть изменения внесенные в файлы "*.html, *.js, *.css" необходимо нажать:
//1. CTRL+SHIFT+DELL  2.CTRL+F5  3.CTRL+SHIFT+R

//!!!Будет использовано два типа написания Ajax запросов: с использованием JQuery+JS и чистое написание на JS.

//Блок написания JQuery+JS(позволяет не тестировать написанный код на разных браузерах, будет работать везде)!!!



function loadAllFilesInfo() {

               $.ajax({
                         type: "GET",
                         url: "https://localhost:443/filesInfo/findAllFilesName",
//                         url: "http://localhost:8080/filesInfo/findAllFilesName",
                         dataType: 'json',
                         success:  function (filesName, status, xhr) {
                            let htmlAll = '<th>File name</th>\n' +
                                          '<th>Download</th>\n' +
                                          '<th>Delete</th>\n';
                            for (let name of filesName) {
                                html =  '<tr>' +
                                            '<td>' + name + '</td>\n' +
                                            '<td><button onclick="downloadFileInfo(&quot;' + name + '&quot;)">Download</button></td>' + //т.к. "name" это имя файла с расширением через точку, то его необходимо помещать в кавычки (которые в HTML обозначаются "&quot;")
                                            '<td><button onclick="deleteFileInfo(&quot;' + name + '&quot;)">Delete</button></td>' +
                                        '</tr>\n';
                                htmlAll = htmlAll + html;
                            }
                            document.getElementById("filesInfoList").innerHTML = htmlAll;
//                              $("#filesInfoList").html(htmlAll);  //почему-то тут не работает
                         },
                         error: function (jqXHR, exception) {
                                  myError(jqXHR, exception);
                         }
               });
 }

function downloadFileInfo(fileName) {
         window.location.assign("https://localhost:443/filesInfo/download?param1=" + fileName);
//         window.location.assign("http://localhost:8080/filesInfo/download?param1=" + fileName);
// если использовать на сервере механизм @PathVariable, то -  window.location.assign("http://localhost:8080/filesInfo/download/" + fileName);
}

function deleteFileInfo(fileName) {
             $.ajax({
                        type: "DELETE",
                        url: "https://localhost:443/filesInfo/delete/" + fileName,
//                        url: "http://localhost:8080/filesInfo/delete/" + fileName,
                        error: function (jqXHR, exception) {
                               myError(jqXHR, exception);
                        }
             });

             setTimeout(() => { loadAllFilesInfo(); }, 200);
}


//Тут два варианта написания для получения параметра: через @RequestParam или @PathVariable
function downloadFile() {
    let fileName = $("#download_field").val();
    if(fileName.length < 4) {
    alert('minimum length "file name with extension" field 4 symbols');
//         window.location.assign("http://localhost:8080/filesInfo/download?param1=empty");
//         window.location.assign("http://localhost:8080/users/download/empty");

    } else {
         window.location.assign("https://localhost:443/filesInfo/download?param1=" + fileName);
//         window.location.assign("http://localhost:8080/filesInfo/download?param1=" + fileName);
//         window.location.assign("http://localhost:8080/filesInfo/download/" + fileName);
    }
}



function uploadFile() {

    $("#result").html(""); //очищаем поле если там уже был результат

    if (window.FormData === undefined) {
		alert('В вашем браузере FormData не поддерживается');
	} else if ($("#js-file").val().length < 3) {
	    alert("Not file for download");
	} else {
		let formData = new FormData();
		formData.append('file', $("#js-file")[0].files[0]);

//Чтобы после отправки клиентом файла у него опять не раскрывалось окно с просьбой выбрать очередной файл для отправки на сервер:
		    event.stopPropagation(); // Остановка механизма отправки информации с полей ввода у клиента
            event.preventDefault();  // Полная остановка механизма отправки информации с полей ввода у клиента

		$.ajax({
			type: "POST",
			url: "https://localhost:443/filesInfo/upload",
//			url: "http://localhost:8080/filesInfo/upload",
			cache: false,
			contentType: false,
			processData: false,
			data: formData,
			success: function( resp, textStatus, jqXHR ){
                        // Если все ОК
                        if( typeof resp.error === 'undefined' ){
                            $("#result").html(resp);
                             setTimeout(() => { $("#result").html(""); }, 1300); //очищаем поле чрез 15 секунд
                        }
                        else{
                            console.log('ОШИБКИ ОТВЕТА сервера: ' + respond.error );
                        }
            },
            error: function (jqXHR, exception) {
                         myError(jqXHR, exception);
            }
		});

		setTimeout(() => { loadAllFilesInfo(); }, 1500);
	}
}

