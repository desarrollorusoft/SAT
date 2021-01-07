function filtrarCuentas( tributo ) {
  var input, filter, table, tr, td, i;
  input = document.getElementById("filtroCuentas-" + tributo);
  filter = input.value.toUpperCase();
  table = document.getElementsByClassName("tabla-filtrar-"+ tributo);
  	
  	for( var i = 0; i < table.length; i++ ){
	  tr = table[i].getElementsByTagName("tr");
	  for (i = 0; i < tr.length; i++) {
	    td = tr[i].getElementsByTagName("td");
	    if (td.length > 0) {
	      if (td[0].innerHTML.toUpperCase().indexOf(filter) > -1 ||
	    		  td[1].innerHTML.toUpperCase().indexOf(filter) > -1 ||
	    		  td[2].innerHTML.toUpperCase().indexOf(filter) > -1 ) {
	        tr[i].style.display = "";
	      } else {
	        tr[i].style.display = "none";
	      }
	    } 
	  }
	}  
}

function filtrarCuentasGrilla() {
  var input, filter, cards, card, i;
  input = document.getElementById("filtroCuentasGrilla");
  filter = input.value.toUpperCase();
  cards = document.getElementsByClassName("card-cuenta");

	for( var i = 0; i < cards.length; i++ ){
	  card = cards[i];

	  if( card.getElementsByClassName("alias-cuenta")[0].innerHTML.toUpperCase().indexOf(filter) > -1 ||
			  card.getElementsByClassName("dato-cuenta")[0].innerHTML.toUpperCase().indexOf(filter) > -1 ||
			  card.getElementsByClassName("monto-deuda")[0].innerHTML.toUpperCase().indexOf(filter) > -1 ||
			  card.getElementsByClassName("monto-vencer")[0].innerHTML.toUpperCase().indexOf(filter) > -1 ){

		  card.style.display = "";
	  }
	  else{
		  card.style.display = "none";
	  }
	}
}

function filtrarPlanes() {
	var input, filter, table, tr, td, i;
	input = document.getElementById("filtroPlanes");
	filter = input.value.toUpperCase();
	table = document.getElementsByClassName("tabla-filtrar-plan" );

	for( var i = 0; i < table.length; i++ ){
		tr = table[i].getElementsByTagName("tr");
		for (i = 0; i < tr.length; i++) {
			td = tr[i].getElementsByTagName("td");
			if (td.length > 0) {
				if (td[0].innerHTML.toUpperCase().indexOf(filter) > -1 ||
					td[1].innerHTML.toUpperCase().indexOf(filter) > -1 ||
					td[2].innerHTML.toUpperCase().indexOf(filter) > -1 ) {
					tr[i].style.display = "";
				} else {
					tr[i].style.display = "none";
				}
			}
		}
	}
}