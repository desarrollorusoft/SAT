function stopRKey(evt) {
	var evt = (evt) ? evt : ((event) ? event : null); 
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
	
	/* if ((evt.keyCode == 13) && (node.type=="text"))  {return false;} */ 
	
	if ( (evt.keyCode == 13) && 
		 ((node.type == "text") || (node.type == "password") || (node.type == "radio") || (node.type == "checkbox")) )  {
		
		return false;
	}
}

function controlV(event){
	if( event.target.classList.contains('no-copy-paste') && event.ctrlKey && event.keyCode==86){
		return false;
	
	}
	
}

document.onkeydown = controlV;
document.onkeypress = stopRKey;