function checkForm(form){
    let formularOk = true;
    // Ein Geschlecht muss gewaehlt sein
    if (!(document.getElementById("weiblich").checked || document.getElementById("mann").checked)){
        alert("Sie muessen unbedingt ein Geschlescht auswählen");
        formularOk = false;
    } else {
        // Append a green background to the following page if the user is female
        let file = new File("style.css");
        file.open();
        file.writeln(".weiblich { background-color: pink; }");
        file.writeln(".mann { background-color: green; }");
        file.close();
    }
    // Name muss mindestens zwei Buchstaben haben
    let userName = (form.userName).value;
    if((userName.trim()).length < 2){
        alert ("Der Name muss aus mindestens 2 Buchstaben bestehen!");
        formularOk = false;
    }

    // Wir pruefen, ob das Land in den vorgegebenen Laender enethalten ist.
    let laender = ["Deutschland", "Belgien", "China", "Frankreich", "Moroco", "Tunesien" ];
    let land = (form.land).value;
    if(laender.indexOf(land) === -1){
        alert ("Waehlen Sie ein Land aus der Liste aus!");
        formularOk = false;
    }

    // Link fuer naeschte Seite falls das Formular in Ordnung ist.
    if(formularOk){
        let text = document.createElement("h2");
        text.appendChild(document.createTextNode("Ihr Fornular ist gut ausgeffuelt! Druecken Sie auf den unten stehenden link" +
            "  um auf die neaschste Seite zu gehen.")) ;
        document.body.append(text);

        // Der link
        let link = document.createElement("a");
        link.href="page1.html";
        link.innerHTML = "Weiter";
        link.className="link";
        document.body.append(link);
    }
    //

}