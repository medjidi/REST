"use strict";
console.log(1 + 1);
const url = 'http://localhost:3333/admin';
const postList = document.querySelector("tbody");
const collapse = document.querySelector(".collapse");
const creatUser = document.querySelector("#creater")
const listOfRoles = document.querySelector("select")
const konetsBlizok = document.querySelector("#nemozhetbuit");

const firstName = document.getElementById('firstName');
const lastName = document.getElementById('lastName');
const age = document.getElementById('age');
const email = document.getElementById('email');
const password = document.getElementById('password');
const rolesUser = document.getElementById('editRoles');


let roles = [];
const renderPosts = (data) => {
    let table = '';
    let coll = '';
    let rolesA = '';
    console.log(data);
    let admin = data.admin;

    coll += `<div>
            <span class="nav-text text-navb probel">${admin.email}</span>
            <span class="nav-text probel">with roles:</span>`;

    admin.roles.forEach(role => {
        coll += `<span class="nav-text probel"> ${role.name}</span>`;
    })
    coll += `</div>`;
    collapse.innerHTML = coll;
    roles = data.roles;
    roles.forEach(role => {
        rolesA += `<option value="${role.id}">${role.name}
                     </option>`;
    });
    listOfRoles.innerHTML = rolesA;
    data.users.forEach(post => {
        table += `<tr data-id="${post.id}">
            <td> ${post.id}</td>
            <td> ${post.firstName}</td>
            <td> ${post.lastName}</td>
            <td> ${post.age}</td>
            <td> ${post.email}</td>

            <td>`;
        post.roles.forEach(role => {
            table += `<span>${role.name} </span>`;
        });
        table += `</td>
                <td>
                <button type="button" class="btn btn-info" data-bs-toggle="modal"
                        data-bs-target="${'#exampleModal' + post.id}" >
                    Edit
                </button>

                <div class="modal fade" id="${'exampleModal' + post.id}" tabIndex="-1"
                     aria-labelledby="exampleModalLabel" style="display: none;" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLabel">Edit
                                    user</h5>
                                <button type="button" class="btn-close"
                                        data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <form align="center" id="formof${post.id}">
                                <div class=" modal-body row justify-content-md-center">
                                    <div class="col-md-7">
                                        <p class="form-label text-navb">Id </p>
                                        <input type="text" class="form-control"
                                               value="${post.id}" name="id" disabled id="idEdit${post.id}"/>
                                    </div>
                                    <p></p>
                                    <div class="col-md-7">
                                        <p class="form-label text-navb">First
                                            name </p>
                                        <input type="text" class="form-control"
                                               value="${post.firstName}"
                                               name="firstName" id="firstNameEdit${post.id}"/>
                                    </div>
                                    <p></p>
                                    <div class="col-md-7">
                                        <p class="form-label text-navb">Last
                                            name </p>
                                        <input type="text" class="form-control"
                                               value="${post.lastName}"
                                               name="lastName" id="lastNameEdit${post.id}"/>
                                    </div>
                                    <p></p>

                                    <div class="col-md-7">
                                        <p class="form-label text-navb">Age</p>
                                        <input type="number" class="form-control"
                                               value="${post.age}"
                                               name="age" id="ageEdit${post.id}"/>
                                    </div>
                                    <p></p>

                                    <div class="col-md-7">
                                        <p class="form-label text-navb">Email </p>
                                        <input type="email" class="form-control"
                                               value="${post.email}"
                                               name="email" id="emailEdit${post.id}"/>
                                    </div>
                                    <p></p>

                                    <div class="col-md-7">
                                        <p class="form-label text-navb">Password</p>
                                        <input type="password" class="form-control"
                                               name="password" id="passwordEdit${post.id}"/>
                                    </div>
                                    <p></p>

                                    <div class="form-group col-md-7">
                                        <p class="form-label text-navb">Roles</p>

                                        <select multiple class="form-control"
                                                name="roles"
                                                size="2"
                                                id="rolesEdit${post.id}">`
        table += rolesA;
        table += `         </select>
                                    </div>
                                </div>
                                <div class="modal-footer" assign="right">
                                    <button type="button" class="btn btn-secondary"
                                            data-bs-dismiss="modal" id="close">Close
                                    </button>
                                    <button type="submit" class="btn btn-success btn" id="edit-post">Edit
                                    </button>
                                </div>
                            </form>

                        </div>
                    </div>
                </div>

            </td>

            <td>
                <button type="submit" class="btn btn-danger" id="delete-post">
                    Delete
                </button>
            </td>
    
            </tr>`;
    });
    postList.innerHTML = table;
    let UserPage = `<tr>
            <td >${admin.id}</td>
            <td >${admin.firstName}</td>
            <td >${admin.lastName}</td>
            <td >${admin.age}</td>
            <td >${admin.email}</td>

            <td>`
    admin.roles.forEach(role => {
        UserPage += `<span class="nav-text probel"> ${role.name}</span>`;
    });
    UserPage+= `</tr>`;
    konetsBlizok.innerHTML = UserPage;
}


fetch(url).then(res => res.json())
    .then(data => renderPosts(data))


let userid;
postList.addEventListener('click', (e) => {
    e.preventDefault();
    let delButtonPressed = e.target.id == `delete-post`;
    let editButtonPressed = e.target.id == `edit-post`;
    console.log('button');
    if (!!e.target.parentElement.parentElement.dataset.id) {
        userid = e.target.parentElement.parentElement.dataset.id;
    }
    console.log(userid);
    if (delButtonPressed) {
        fetch(`${url}/${userid}`, {
            method: 'DELETE',
        }).then(res => res.json())
            .then(data => renderPosts(data))

    } else if (editButtonPressed) {

        const firstNameEdit = document.getElementById(`firstNameEdit${userid}`);
        const lastNameEdit = document.getElementById(`lastNameEdit${userid}`);
        const ageEdit = document.getElementById(`ageEdit${userid}`);
        const emailEdit = document.getElementById(`emailEdit${userid}`);
        const passwordEdit = document.getElementById(`passwordEdit${userid}`);
        const rolesUserEdit = document.getElementById(`rolesEdit${userid}`);
        console.log(firstNameEdit);
        console.log(lastNameEdit);
        console.log(ageEdit);
        console.log(emailEdit);
        console.log(passwordEdit);
        console.log(rolesUserEdit);

        let getrole = (id) => {
            for (const op of roles) {
                if (op.id == id) {
                    return op.authority;
                }
            }
        }

        let strs = [];
        for (const option of rolesUserEdit.selectedOptions) {
            strs.push({
                id: option.value,
                name: getrole(option.value)
            });
        }

        const d = {
            id: userid,
            firstName: firstNameEdit.value,
            lastName: lastNameEdit.value,
            age: ageEdit.value,
            email: emailEdit.value,
            password: passwordEdit.value,
            roles: strs
        };
        console.log(d);
        fetch(`${url}/${userid}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(d)
        }).then(res => res.json())
            .then(data => renderPosts(data))
        $('.modal-backdrop').remove();
    }
})


creatUser.addEventListener('submit', (e) => {

    e.preventDefault();


    let getrole = (id) => {
        for (const op of roles) {
            if (op.id == id) {
                return op.authority;
            }
        }
    }

    let strs = [];
    for (const option of rolesUser.selectedOptions) {
        console.log(option);
        strs.push({
            id: option.value,
            name: getrole(option.value)
        });
    }
    console.log('Строки: ' + strs);
    const d = {
        firstName: firstName.value,
        lastName: lastName.value,
        age: age.value,
        email: email.value,
        password: password.value,
        roles: strs
    };
    console.log(d);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(d)
    }).then(res => res.json())
        .then(data => renderPosts(data))
})














