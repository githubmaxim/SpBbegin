//Для всех кнопок используем вариант привязки Функций к кнопкам "<button onclick=" в файле *.html.
//Возможно написание через "$( document ).ready(function(){ $( "button" ).click(function(){---}" в файле *.js. - у меня не получился!

//Чтобы в броузере "Chrome" в "localhost:8080" увидеть изменения внесенные в файлы "*.html, *.js, *.css" необходимо нажать:
//1. CTRL+SHIFT+DELL  2.CTRL+F5  3.CTRL+SHIFT+R

//!!!Будет использовано два типа написания Ajax запросов: с использованием JQuery+JS и чистое написание на JS.

//Блок написания JQuery+JS(позволяет не тестировать написанный код на разных браузерах, будет работать везде)!!!
function findByLogin() {
    let login = $("#search_field").val();

   $.ajax({
           type: "GET",
           url: "http://localhost:8080/users/findByLogin?param1="+login,
//или так  url: "http://localhost:8080/users/findByLogin?param1="+ $("#search_field").val(),
           dataType: 'json',
           success:  function (user, status, xhr) {
                let html1 = '<table>' +
                               '<tr>\n' +
                                 '<th>User id</th>\n' +
                                  '<th>User name</th>\n' +
                                   '<th>User login</th>\n' +
                                   '<th>User email</th>\n' +
                                   '<th>Delete</th>\n' +
                               '</tr>' +
                               '<tr>\n' +
                                 '<td>' + user.id + '</td>\n' +
                                 '<td>' + user.name + '</td>\n' +
                                  '<td>' + user.login + '</td>\n' +
                                '<td>' + user.email + '</td>' +
                                '<td><button onclick="deleteUser(' + user.id + ')">Delete</button></td>' +
                               '</tr>' +
                           '</table>';
                 $("#usersList").html(html1);
//                 document.getElementById("usersList").innerHTML = html1;
           },
           error: function (jqXHR, exception) {
                    myError(jqXHR, exception);
           }
   });
}


function loadAllUsers() {
               $.ajax({
                         type: "GET",
                         url: "http://localhost:8080/users/findAll",
                         dataType: 'json',
                         success:  function (users, status, xhr) {
                            let htmlAll = '<th>User id</th>\n' +
                                          '<th>User name</th>\n' +
                                          '<th>User login</th>\n' +
                                          '<th>User email</th>\n' +
                                          '<th>Delete</th>\n';
                            for (let user of users) {
                                html =  '<tr>' +
                                            '<td>' + user.id + '</td>\n' +
                                            '<td>' + user.name + '</td>\n' +
                                            '<td>' + user.login + '</td>\n' +
                                            '<td>' + user.email + '</td>' +
                                            '<td><button onclick="deleteUser(' + user.id + ')">Delete</button></td>' +
                                        '</tr>\n';
                                htmlAll = htmlAll + html;
                            }
                            document.getElementById("usersList").innerHTML = htmlAll;
//                              $("#usersList").html(htmlAll);  //почему-то тут не работает
                         },
                         error: function (jqXHR, exception) {
                                  myError(jqXHR, exception);
                         }
               });
 }

function deleteUser(userId) {
             $.ajax({
                        type: "DELETE",
                        url: "http://localhost:8080/users/delete/" + userId,
                        error: function (jqXHR, exception) {
                               myError(jqXHR, exception);
                        }
             });

             setTimeout(() => { loadAllUsers(); }, 200);
}


//Тут два варианта написания для получения параметра: через @RequestParam или @PathVariable
function downloadFile() {
    let fileName = $("#download_field").val();
    if(fileName.length < 3) {
         window.location.assign("http://localhost:8080/filesInfo/download?param1=empty");
//         window.location.assign("http://localhost:8080/users/download?param1=empty");
//         window.location.assign("http://localhost:8080/users/download/empty");

    } else {
         window.location.assign("http://localhost:8080/filesInfo/download?param1=" + fileName);
//         window.location.assign("http://localhost:8080/users/download?param1=" + fileName);
//         window.location.assign("http://localhost:8080/users/download/" + fileName);

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

//Чтобы после отпраки клиентом файла у него опять не раскрывалось окно с просьбой выбрать очередной файл для отправки на сервер:
		    event.stopPropagation(); // Остановка механизма отправки информации с полей ввода у клиента
            event.preventDefault();  // Полная остановка механизма отправки информации с полей ввода у клиента

		$.ajax({
			type: "POST",
			url: "http://localhost:8080/filesInfo/upload",
//			url: "http://localhost:8080/users/upload",
			cache: false,
			contentType: false,
			processData: false,
			data: formData,
			success: function( resp, textStatus, jqXHR ){
                        // Если все ОК

                        if( typeof resp.error === 'undefined' ){
                            $("#result").html(resp);
                             setTimeout(() => { $("#result").html(""); }, 1500); //очищаем поле чрез 15 секунд
                        }
                        else{
                            console.log('ОШИБКИ ОТВЕТА сервера: ' + respond.error );
                        }
            },
            error: function (jqXHR, exception) {
                         myError(jqXHR, exception);
            }
		});
	}
}


function createUser() {
           let userName = $("#user_name").val();
           let userLogin = $("#user_login").val();
           let userEmail = $("#user_email").val();
           let userUniversityName = $("#user_university_name").val();
           let userUniversityCity = $("#user_university_city").val();
           let user = {
                    name: userName,
                    login: userLogin,
                    email: userEmail,
                    universities: [
                                    { name: userUniversityName},
                                    {
                                      cities: {
                                              city: userUniversityCity
                                      }
                                    }
                    ]
           }
           if (validateOnSpace(userName)) {alert('remove the space in the "User name" field');
           } else if (validateLength(userName)) {alert('minimum length "User name" field 3 symbols');
           } else if (!validateFirstSymbol(userName)) {alert('first symbol "User name" field should be a letter');
           } else if (!validateLetterNumeral(userName)) {alert('"User name" field must contain only letters or numbers');
           } else if (validateOnSpace(userLogin)) {alert('remove the space in the "User login" field');
           } else if (validateLength(userLogin)) {alert('minimum length "User login" field 3 symbols');
           } else if (!validateFirstSymbol(userLogin)) {alert('first symbol "User login" field should be a letter');
           } else if (!validateLetterNumeral(userLogin)) {alert('"User login" field must contain only letters or numbers');
           } else if (validateLength(userEmail)) {alert('fill in the "User email" field');
           } else if (!validateEmail(userEmail)) { alert("incorrect email");//эта проверка должна быть последней, т.к. после нее неправильно срабатывает первая проверка на заглавные буквы, а вторая уже нормально
           } else {
               $.ajax({
                       type: "POST",
                       url: "http://localhost:8080/users/save",
                       data: JSON.stringify(user),
                       contentType: 'application/json',
                       success: function( resp, textStatus, jqXHR ){
                                               // Если все ОК

                                               if( typeof resp.error === 'undefined' ){
                                                   $("#created").html(resp);
                                                    setTimeout(() => { $("#created").html(""); }, 1500); //очищаем поле чрез 15 секунд
                                               }
                                               else{
                                                   console.log('ОШИБКИ ОТВЕТА сервера: ' + respond.error );
                                               }
                       },
//если действие не прошло (т.е. мы не получили код 200), и получили другой код, то обработать это событие я смог только
//в блоке ошибок (по отправляемому собой статусу с кодом 444). Либо тут же (из того же jqXHR) можно получить отправляемое сервером сообщение - "alert(jqXHR.responseText);"!!!
                       error: function (jqXHR, exception) {
                               myError(jqXHR, exception);
                       }
               });
               setTimeout(() => { loadAllUsers(); }, 1600);
           }
}

//эта проверка должна быть последней, т.к. после нее неправильно срабатывает первая проверка на заглавные буквы, а вторая уже нормально
function validateEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

function validateOnSpace(name) {
    const re = /^\s|\s$/;
    return re.test(name);
}

function validateLength(name) {
    return name.length <3;
}

function validateFirstSymbol(name) {
    const re = /^[a-zA-Z]/;
    return re.test(name);
}

function validateLetterNumeral(name) {
    const re = /^[A-Za-z0-9]+$/;
    return re.test(name);
}



function myError(jqXHR, exception) {
                if (jqXHR.status === 0) {
           			alert('Not connect. Verify Network.');
           		} else if (jqXHR.status == 404) {
           			alert('Requested page not found (404).');
           		} else if (jqXHR.status == 444) {
           			alert('Such login is exist (444).');
           		} else if (jqXHR.status == 500) {
           			alert('Internal Server Error (500).');
           		} else if (exception === 'parsererror') {
           			alert('Requested JSON parse failed.');
           		} else if (exception === 'timeout') {
           			alert('Time out error.');
           		} else if (exception === 'abort') {
           			alert('Ajax request aborted.');
           		} else {
           			alert('Uncaught Error. ' + jqXHR.responseText);
           		}
}




//Блок написания - чистый JS(скорее всего нужно будет тестировать работу с ним разных браузеров)!!!
//     function searchByLogin() {
//          let login = document.getElementById("search_field").value;
//          let xhttp = new XMLHttpRequest();
//          xhttp.onreadystatechange = function () {
//                 if (this.readyState == 4 && this.status == 200) {
//                    let user = JSON.parse(this.responseText);
//                    let html = '<table>' +
//                                  '<tr>\n' +
//                                    '<th>User id</th>\n' +
//                                     '<th>User name</th>\n' +
//                                      '<th>User login</th>\n' +
//                                      '<th>User email</th>\n' +
//                                      '<th>Delete</th>\n' +
//                                  '</tr>' +
//                                  '<tr>\n' +
//                                    '<td>' + user.id + '</td>\n' +
//                                    '<td>' + user.name + '</td>\n' +
//                                     '<td>' + user.login + '</td>\n' +
//                                   '<td>' + user.email + '</td>' +
//                                   '<td><button onclick="deleteUser(' + user.id + ')">Delete</button></td>' +
//                                  '</tr>' +
//                              '</table>';
//                     document.getElementById("usersList").innerHTML = html;
//                 }
//          }
//          xhttp.open("GET", "http://localhost:8080/users/findByLogin?param1="+login, true);
//          xhttp.send();
//        }
//
//        function loadAllUsers() {
//              let xhttp = new XMLHttpRequest();
//              xhttp.onreadystatechange = function () {
//                 if (this.readyState == 4 && this.status == 200) {
//                    let users = JSON.parse(this.responseText);
//                    let htmlAll = '<th>User id</th>\n' +
//                                  '<th>User name</th>\n' +
//                                  '<th>User login</th>\n' +
//                                  '<th>User email</th>\n' +
//                                  '<th>Delete</th>\n';
//                    for (let user of users) {
//                          html =    '<tr>' +
//                                        '<td>' + user.id + '</td>\n' +
//                                        '<td>' + user.name + '</td>\n' +
//                                        '<td>' + user.login + '</td>\n' +
//                                        '<td>' + user.email + '</td>' +
//                                        '<td><button onclick="deleteUser(' + user.id + ')">Delete</button></td>' +
//                                    '</tr>\n';
//                          htmlAll = htmlAll + html;
//                    }
//                    document.getElementById("usersList").innerHTML = htmlAll;
//                 }
//              };
//           xhttp.open("GET", "http://localhost:8080/users/findAll", true);
//           xhttp.send();
//        }
//
//
//        function deleteUser(userId) {
//            let xhttp = new XMLHttpRequest();
//            xhttp.open("DELETE",
//                       "http://localhost:8080/users/delete/" + userId,
//                       true);
//            xhttp.send();
//            setTimeout(() => { loadAllUsers(); }, 200);
//        }
//
//
//         function createUser() {
//            let userName = document.getElementById("user_name").value;
//            let userLogin = document.getElementById("user_login").value;
//            let userEmail = document.getElementById("user_email").value;
//
//            let xmlhttp = new XMLHttpRequest();
//            xmlhttp.open("POST",
//                         "http://localhost:8080/users/save");
//            xmlhttp.setRequestHeader("Content-Type", "application/json");
//            xmlhttp.send(JSON.stringify({name: userName, login: userLogin,
//                                                       email: userEmail}));
//            setTimeout(() => { loadAllUsers(); }, 200);
//        }


