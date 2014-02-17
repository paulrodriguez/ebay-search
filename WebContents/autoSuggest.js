var xmlHttp = new XMLHttpRequest();


function AutoSuggestionControl(oTextBox, oResultBox,oProvider)
{
	this.cur = -1;
	this.layer = null;
	this.provider = oProvider;
	this.resultbox = oResultBox;
	this.textbox = oTextBox;
	this.init();
};
//for keyup event
AutoSuggestionControl.prototype.handleKeyUp = function(oEvent) {

	
	var iKeyCode = oEvent.keyCode;
	//if backspace or delete key is pressed
	if(iKeyCode == 8 || iKeyCode == 46) {
		this.cur = -1;//reset position of suggestion if a new word is entered
		//do not create type ahead word
		this.provider.sendAjaxRequest(this,false);
	}
	if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode <= 46) || (iKeyCode >= 112 && iKeyCode <= 123)) 
    {
       //do nothing
    } 
    else 
    {
		
		this.cur = -1; //reset position of suggestion if a new word is entered
		this.provider.sendAjaxRequest(this,false);//change to true if want type ahead
    }

	
	//	this.provider.sendAjaxRequest(this);
};

//if the user focuses out of the text box then remove the suggestion box
AutoSuggestionControl.prototype.erase = function() {
	this.resultbox.style.visibility = "hidden";
};

AutoSuggestionControl.prototype.init = function() {
	var oThis = this;
	
	this.textbox.onkeyup = function(oEvent) {
		if(!oEvent)
		{
			oEvent = window.event;
		}
		oThis.handleKeyUp(oEvent);	
	}
	
	this.textbox.onkeydown = function (oEvent) 
	{
        if (!oEvent) 
        {
            oEvent = window.event;
        } 

        oThis.handleKeyDown(oEvent);
    };

	
	this.textbox.onblur = function() {
		oThis.erase();
	}
	
		this.createDropDown();
};


AutoSuggestionControl.prototype.createDropDown = function () 
{
    
    this.resultbox.className = "resultsbox";
    this.resultbox.style.visibility = "hidden";
   
    var oThis = this;

    this.resultbox.onmousedown = this.resultbox.onmouseup = this.resultbox.onmouseover = function (oEvent) 
    {
        oEvent = oEvent || window.event;
        oTarget = oEvent.target || oEvent.srcElement;

        if (oEvent.type == "mousedown") 
        {
            oThis.textbox.value = oTarget.firstChild.nodeValue;
            oThis.resultbox.style.visibility="hidden";
        }
        else if (oEvent.type == "mouseover") 
        {
            oThis.highlightSuggestion(oTarget);
        } 
        else 
        {
            oThis.textbox.focus();
        }
    }
};

AutoSuggestionControl.prototype.handleKeyDown = function(oEvent) {
switch(oEvent.keyCode) 
    {
        case 38: //up arrow
            this.previousSuggestion();
            break;
        case 40: //down arrow 
            this.nextSuggestion();
            break;
        case 13: //enter
            this.erase();
            break;
    }

};

AutoSuggestionControl.prototype.selectRange = function(start, end) {
	
	this.textbox.setSelectionRange(start, end);
	this.textbox.focus();
};

AutoSuggestionControl.prototype.typeAhead = function(suggestion) {
	if(this.textbox.setSelectionRange) {
		var curValue = this.textbox.value;
		if(suggestion.indexOf(curValue) == 0) {
			var iLen = this.textbox.value.length;
			this.textbox.value = suggestion;
			this.selectRange(iLen, suggestion.length);
			
		}
	}
};

AutoSuggestionControl.prototype.autoFill = function(suggestions,fill_in) {
	if(fill_in) {
		this.typeAhead(suggestions);
	}	
}

AutoSuggestionControl.prototype.previousSuggestion = function() {
	var totalsuggestions = this.resultbox.childNodes;
	if(this.cur > 0 && totalsuggestions.length > 0) {
		    var oNode = totalsuggestions[--this.cur];
			this.highlightSuggestion(oNode);
			this.textbox.value = oNode.firstChild.nodeValue; 
	}
};

function QuerySuggestions() 
{

};




QuerySuggestions.prototype.sendAjaxRequest = function(suggest,isFillIn)  {
	if(xmlHttp && suggest.textbox.value.length > 0) 
	{
		if(xmlHttp.readyState != 4) xmlHttp.abort();
		var txtinput = suggest.textbox.value.replace(/ /g,'+');
		var request = "suggest?q="+txtinput;//encodeURIComponent(suggest.textbox.value);
		
		
		xmlHttp.open("GET", request); 
		xmlHttp.onreadystatechange = function() {
			
			if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
				
				var s = xmlHttp.responseXML.getElementsByTagName('CompleteSuggestion');
				var suggestion=suggest.resultbox;
				if(s.length == 0) {
					suggestion.style.visibility="hidden";
				}
				else suggestion.style.visibility="visible";
				
				suggestion.innerHTML = "";
				var suggestions = "";
				if(s.length > 0) {
					suggestions = s[0].childNodes[0].getAttribute("data");
					suggest.autoFill(suggestions,isFillIn);
				}
				
				for (i = 0; i < s.length; i++) {
					div = document.createElement("div");
					div.style.cursor="pointer";
					
					text = document.createTextNode(s[i].childNodes[0].getAttribute("data"));
					
					div.appendChild(text);
					
					suggestion.appendChild(div);
				}//end for loop
			}
		}	//end onreadystatechange
		xmlHttp.send(null);
	} else if(suggest.textbox.value.length == 0) suggest.resultbox.style.visibility = "hidden";
 };
 
 AutoSuggestionControl.prototype.highlightSuggestion = function (oSuggestionNode) 
{
	
    for (var i = 0; i < this.resultbox.childNodes.length; i++) 
    {
        var oNode = this.resultbox.childNodes[i];
		//if hovering over the selection
        if (oNode == oSuggestionNode) 
        {
            oNode.className = "current";
            this.cur = i;
        }
		//remove class from previous selection
        else if (oNode.className == "current") 
        {
            oNode.className = "";
        }
    }
};

AutoSuggestionControl.prototype.unHighlightSuggestion = function (oSuggestionNode) 
{
	
    for (var i = 0; i < this.resultbox.childNodes.length; i++) 
    {
        var oNode = this.resultbox.childNodes[i];
        if (oNode == oSuggestionNode) 
        {
            oNode.className = "";
            this.cur = -1;
        }
    }
};
 
 AutoSuggestionControl.prototype.nextSuggestion = function () 
{
    var cSuggestionNodes = this.resultbox.childNodes;

    if (cSuggestionNodes.length > 0 && this.cur < cSuggestionNodes.length - 1) 
    {
        var oNode = cSuggestionNodes[++this.cur];
        this.highlightSuggestion(oNode);
        this.textbox.value = oNode.firstChild.nodeValue; 
    }
};
