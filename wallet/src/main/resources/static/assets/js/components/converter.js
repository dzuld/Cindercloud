(function(){

	BigNumber.config({ EXPONENTIAL_AT: 50 });
	BigNumber.config({ ERRORS: false });

	function init(){
		setFields("ether", new BigNumber(1));
	}

	function setFields(id,v){
		if(id != "wei" ) document.getElementById("wei").value = v.times(new BigNumber(1000000000000000000)).toString();
		if(id != "kwei" ) document.getElementById("kwei").value = v.times(new BigNumber(1000000000000000)).toString();
		if(id != "mwei" ) document.getElementById("mwei").value = v.times(new BigNumber(1000000000000)).toString();
		if(id != "gwei" ) document.getElementById("gwei").value = v.times(new BigNumber(1000000000)).toString();
		if(id != "szabo" ) document.getElementById("szabo").value = v.times(new BigNumber(1000000)).toString();
		if(id != "finney" ) document.getElementById("finney").value = v.times(new BigNumber(1000)).toString();
		if(id != "ether" ) document.getElementById("ether").value = v.times(new BigNumber(1)).toString();
		if(id != "kether" ) document.getElementById("kether").value = v.times(new BigNumber(0.001)).toString();
		if(id != "mether" ) document.getElementById("mether").value = v.times(new BigNumber(0.000001)).toString();
		if(id != "gether" ) document.getElementById("gether").value = v.times(new BigNumber(0.000000001)).toString();
		if(id != "tether" ) document.getElementById("tether").value = v.times(new BigNumber(0.000000000001)).toString();
	}

	function calculate(el){
		setFields(el.id, toEther(el));
		return true;
	}


	function toEther(el){
		var id = el.id;
		var value = new BigNumber(el.value);
		switch(id) {
			case "wei":
				value = value.times(new BigNumber(0.000000000000000001));
				break;
			case "kwei":
				value = value.times(new BigNumber(0.000000000000001));
				break;
			case "mwei":
				value = value.times(new BigNumber(0.000000000001));
				break;
			case "gwei":
				value = value.times(new BigNumber(0.000000001));
				break;
			case "szabo":
				value = value.times(new BigNumber(0.000001));
				break;
			case "finney":
				value = value.times(new BigNumber(0.001));
				break;
			case "ether":
				value = value.times(new BigNumber(1));
				break;
			case "kether":
				value = value.times(new BigNumber(1000));
				break;
			case "mether":
				value = value.times(new BigNumber(1000000));
				break;
			case "gether":
				value = value.times(new BigNumber(1000000000));
				break;
			case "tether":
				value = value.times(new BigNumber(1000000000000));
				break;
			default:
				break;
		}
		return value;
	}

	init();
	document.getElementById("ether").value = 1;

	$('.calculate').on('input', function() {
		calculate(this);
	});

}).call(this);