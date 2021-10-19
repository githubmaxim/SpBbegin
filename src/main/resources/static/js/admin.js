//!!!Не работающий метод разрешения механизмов POST,PUT,DELETE на страницах!!!!
//   Эту строку дописать в html-лист <meta name="csrf-token" content="{{ csrf_token() }}">
//   Эти строки в скрипт:
//      var csrf_token = $('meta[name="csrf-token"]').attr('content');
//
//      function csrfSafeMethod(method) {
//          // these HTTP methods do not require CSRF protection
//          return (/^(GET|HEAD|OPTIONS)$/.test(method));
//      }
//
//       $.ajaxSetup({
//          beforeSend: function(xhr, settings) {
//                if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
//                  xhr.setRequestHeader("anti-csrf-token", csrf_token);
//              }
//            }
//       });

function loadAllLogPas() {
               $.ajax({
                         type: "GET",
                         url: "https://localhost:443/admin/findAll",
//                         url: "http://localhost:8080/admin/findAll",
                         dataType: 'json',
                         success:  function (users, status, xhr) {
                            let htmlAll = '<th>Id</th>\n' +
                                          '<th>Username</th>\n' +
                                          '<th>Role</th>\n' +
                                          '<th>Change Role</th>\n' +
                                          '<th>Change</th>\n' +
                                          '<th>Delete</th>\n';
                            for (let user of users) {
                                html =  '<tr>' +
                                            '<td>' + user.id + '</td>\n' +
                                            '<td>' + user.username + '</td>\n' +
                                            '<td>' + user.roles + '</td>\n' +
                                            '<td><select id="role' + user.id + '" name="select" required><option></option><option value="ADMIN">ADMIN</option><option value="USER">USER</option></select></td>\n' +
                                            '<td><button onclick="changeRoleLogPas(' + user.id + ')">Change Role</button></td>' +
                                            '<td><button onclick="deleteLogPas(' + user.id + ')">Delete</button></td>' +
                                        '</tr>\n';
                                htmlAll = htmlAll + html;
                            }
                            document.getElementById("logpasList").innerHTML = htmlAll;
//                              $("#usersList").html(htmlAll);  //почему-то тут не работает
                         },
                         error: function (jqXHR, exception) {
                                  myError(jqXHR, exception);
                         }
               });
 }


function deleteLogPas(userId) {
             $.ajax({
                        dataType: 'json',
                        type: "DELETE",
                        url: "https://localhost:443/admin/delete/" + userId,
//                        url: "http://localhost:8080/admin/delete/" + userId,
                        error: function (jqXHR, exception) {
                               myError(jqXHR, exception);
                        }
             });
             setTimeout(() => { loadAllLogPas(); }, 200);
//             setTimeout(() => { document.getElementById("usersList2").innerHTML = ""; }, 200);
}


function changeRoleLogPas(userId) {
        let logpas = $("#role"+userId).val();

        $.ajax({
                        type: "PUT",
                        url: "https://localhost:443/admin/put/" + userId,
//                        url: "http://localhost:8080/admin/put/" + userId,
                        contentType: 'application/json',
                        data: {"":logpas},  //одно слово получилось передать на сервер только таким специфическим образом
                        success: function( resp, textStatus, jqXHR ){}
        });
       setTimeout(() => { loadAllLogPas(); }, 1000);
}



