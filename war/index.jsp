<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="de.wortopia.model.*" %>    
<%
    	// Get user from sessio or create new 
    	User user;

    	// Ther might be a better way to do this...
    	{
    		Object oUser;
    		if ((oUser = session.getAttribute("user")) != null) {
    	user = (User)oUser;
    		}
    		else
    		{
    	user = User.getNewAnonUser();
    	session.setAttribute("user", user);
    		}
    	}
    	
    	// Current game
    	long gameId = 0;
    	int size = 4;
    	
    	// Get the current field
    	Field lastField = Field.fetchField(gameId-1, size);
    	Field currentField = Field.fetchField(gameId, size);
    	
    	// Channel token
    	Channel channelToken = new Channel(size);
    %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Wortopia V4</title>
  <meta name="description" content="Multiplayer-Boggle-Variante: Versuche innerhalb von 3 Minuten mehr Wรถrter auf dem Spielfeld zu finden als deine Gegner.">
  <meta name="author" content="Marek Ventur">
  <link rel="stylesheet" href="css/reset.css">
  <link rel="stylesheet" href="css/style.css">
</head>

<body>
  <div id="header">
    <div id="header_content">
      <a href="http://www.wortopia.de" id="logo">Wortopia</a>

      <ul id="nav"> 
        <li><a href="#">Regeln</a></li>
        <li><a href="#">Info</a></li>
        <li><a href="#">Forum</a></li>
      </ul>

      <ul id="nav_mode"> 
        <li><a href="/" id="game4">4x4</a></li>
        <li><a href="/game5" id="game5">5x5</a></li>
      </ul>

      <div class="clear"></div>
    </div>
  </div>
  <div id="content">
	<div id="users">
		<div id="user_info" class="user_logged_in">	
			<form action="/changeUsername" method="post">
				<strong>Hallo <input type="text" name="guest_name" id="guest_name" value="<%= user.getUsername() %>" />.</strong>
			</form>
			
			<% if(user.isAuthenticated()) { %>
			    	<a href="/logout">Logout</a>
					<a href="/account">Deinen Account verwalten</a>
			<% } else { %>
			    	<a href="/login">Login</a>
			<% }  %>
			
		</div>
			
	 </div>
    
    <div id ="gaming_area">
      <div id="field" class="field field4" style="width:400px;">
        <table style="width:400px; height:400px; font-size:400px;">
          <tr>
            <td class="cell_0_0 field_path">A</td>
            <td class="cell_1_0 field_path">B</td>
            <td class="cell_2_0">C</td>
            <td class="cell_3_0">D</td>
          </tr>
          <tr>
            <td class="cell_0_1">E</td>
            <td class="cell_1_1">F</td>
            <td class="cell_2_1 field_path" >G</td>
            <td class="cell_3_1">H</td>
          </tr>
          <tr>
            <td class="cell_0_2">I</td>
            <td class="cell_1_2 field_path_start" >J</td>
            <td class="cell_2_2">K</td>
            <td class="cell_3_2">L</td>
          </tr>
          <tr>
            <td class="cell_0_3">M</td>
            <td class="cell_1_3">N</td>
            <td class="cell_2_3">O</td>
            <td class="cell_3_3">P</td>
          </tr>
        </table>
      </div>
      <input type="text" id="game_input" />

      <div id="chat">
        <div id=chatMessages></div>
        <input type="text" value="" id="chatInput" />  
      </div>

    </div>


    

    <div id="last_game">

      <div id="last_field" class="field field4">
        <table style="width:100px; height:100px; font-size:100px;">
          <tr>
            <td class="cell_0_0 field_path">A</td>
            <td class="cell_1_0 field_path">B</td>
            <td class="cell_2_0">C</td>
            <td class="cell_3_0">D</td>
          </tr>
          <tr>
            <td class="cell_0_1">E</td>
            <td class="cell_1_1">F</td>
            <td class="cell_2_1 field_path" >G</td>
            <td class="cell_3_1">H</td>
          </tr>
          <tr>
            <td class="cell_0_2">I</td>
            <td class="cell_1_2 field_path_start" >J</td>
            <td class="cell_2_2">K</td>
            <td class="cell_3_2">L</td>
          </tr>
          <tr>
            <td class="cell_0_3">M</td>
            <td class="cell_1_3">N</td>
            <td class="cell_2_3">O</td>
            <td class="cell_3_3">P</td>
          </tr>
        </table>
      </div>

      
      <table id="last_game_info">
        <tr>
          <td class="last_game_info_label">Max Punkte:</td>
          <td class="last_game_info_value">56</td>
        </tr>
        <tr> 
          <td class="last_game_info_label">∅ Punkte:</td>
          <td class="last_game_info_value">23</td>
        </tr>
        <tr>   
          <td class="last_game_info_label">Worte:</td>
          <td class="last_game_info_value">20</td>
        </tr>
        <tr>   
          <td class="last_game_info_label">Nicht gefunden:</td>
          <td class="last_game_info_value">14</td>
        </tr>
      </table>
      
      
      <ul id="last_words">


          <li onclick="overlay_word('fit');" class="word word_points_1  word_guessed_wo26171 word_guessed_wo26107 word_guessed_gu1965 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_fit" onmouseover="word_highlight('fit');" onmouseout="word_highlight_leave();">FIT</li> <li onclick="overlay_word('erahn');" class="word word_points_2 word_noguess" id="word_erahn">ERAHN</li> <li onclick="overlay_word('hirn');" class="word word_points_1  word_guessed_wo26107 word_guessed_gu1965" id="word_hirn" onmouseover="word_highlight('hirn');" onmouseout="word_highlight_leave();">HIRN</li> <li onclick="overlay_word('iris');" class="word word_points_1 word_noguess" id="word_iris">IRIS</li> <li onclick="overlay_word('mob');" class="word word_points_1  word_guessed_wo26171 word_guessed_wo26107 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_mob" onmouseover="word_highlight('mob');" onmouseout="word_highlight_leave();">MOB</li> <li onclick="overlay_word('air');" class="word word_points_1  word_guessed_wo26107" id="word_air" onmouseover="word_highlight('air');" onmouseout="word_highlight_leave();">AIR</li> <li onclick="overlay_word('asern');" class="word word_points_2 word_noguess" id="word_asern">ASERN</li> <li onclick="overlay_word('mimin');" class="word word_points_2 word_noguess" id="word_mimin">MIMIN</li> <li onclick="overlay_word('irin');" class="word word_points_1 word_noguess" id="word_irin">IRIN</li> <li onclick="overlay_word('haerm');" class="word word_points_2 word_noguess" id="word_haerm">HAERM</li> <li onclick="overlay_word('thai');" class="word word_points_1 word_noguess" id="word_thai">THAI</li> <li onclick="overlay_word('har');" class="word word_points_1  word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_har" onmouseover="word_highlight('har');" onmouseout="word_highlight_leave();">HAR</li> <li onclick="overlay_word('ahn');" class="word word_points_1  word_guessed_wo26107 word_guessed_gu1965" id="word_ahn" onmouseover="word_highlight('ahn');" onmouseout="word_highlight_leave();">AHN</li> <li onclick="overlay_word('sir');" class="word word_points_1 word_noguess" id="word_sir">SIR</li> <li onclick="overlay_word('rah');" class="word word_points_1 word_noguess" id="word_rah">RAH</li> <li onclick="overlay_word('res');" class="word word_points_1  word_guessed_gu1965" id="word_res" onmouseover="word_highlight('res');" onmouseout="word_highlight_leave();">RES</li> <li onclick="overlay_word('are');" class="word word_points_1  word_guessed_wo26107 word_guessed_gu1965" id="word_are" onmouseover="word_highlight('are');" onmouseout="word_highlight_leave();">ARE</li> <li onclick="overlay_word('rias');" class="word word_points_1 word_noguess" id="word_rias">RIAS</li> <li onclick="overlay_word('hin');" class="word word_points_1  word_guessed_wo26107 word_guessed_gu1965" id="word_hin" onmouseover="word_highlight('hin');" onmouseout="word_highlight_leave();">HIN</li> <li onclick="overlay_word('haesin');" class="word word_points_3 word_noguess" id="word_haesin">HAESIN</li> <li onclick="overlay_word('hirnt');" class="word word_points_2 word_noguess" id="word_hirnt">HIRNT</li> <li onclick="overlay_word('firn');" class="word word_points_1 word_noguess" id="word_firn">FIRN</li> <li onclick="overlay_word('firm');" class="word word_points_1  word_guessed_wo26171 word_guessed_gu1965" id="word_firm" onmouseover="word_highlight('firm');" onmouseout="word_highlight_leave();">FIRM</li> <li onclick="overlay_word('hai');" class="word word_points_1  word_guessed_wo26171 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_hai" onmouseover="word_highlight('hai');" onmouseout="word_highlight_leave();">HAI</li> <li onclick="overlay_word('arm');" class="word word_points_1 word_noguess" id="word_arm">ARM</li> <li onclick="overlay_word('sah');" class="word word_points_1 word_noguess" id="word_sah">SAH</li> <li onclick="overlay_word('sahne');" class="word word_points_2  word_guessed_wo26107" id="word_sahne" onmouseover="word_highlight('sahne');" onmouseout="word_highlight_leave();">SAHNE</li> <li onclick="overlay_word('harm');" class="word word_points_1  word_guessed_gu1965" id="word_harm" onmouseover="word_highlight('harm');" onmouseout="word_highlight_leave();">HARM</li> <li onclick="overlay_word('hae');" class="word word_points_1 word_noguess" id="word_hae">HAE</li> <li onclick="overlay_word('sarin');" class="word word_points_2  word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_sarin" onmouseover="word_highlight('sarin');" onmouseout="word_highlight_leave();">SARIN</li> <li onclick="overlay_word('harn');" class="word word_points_1  word_guessed_wo26107 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_harn" onmouseover="word_highlight('harn');" onmouseout="word_highlight_leave();">HARN</li> <li onclick="overlay_word('hirne');" class="word word_points_2  word_guessed_wo26107 word_guessed_gu1965" id="word_hirne" onmouseover="word_highlight('hirne');" onmouseout="word_highlight_leave();">HIRNE</li> <li onclick="overlay_word('hit');" class="word word_points_1  word_guessed_wo26171 word_guessed_wo26107 word_guessed_gu1965" id="word_hit" onmouseover="word_highlight('hit');" onmouseout="word_highlight_leave();">HIT</li> <li onclick="overlay_word('his');" class="word word_points_1 word_noguess" id="word_his">HIS</li> <li onclick="overlay_word('hirni');" class="word word_points_2 word_noguess" id="word_hirni">HIRNI</li> <li onclick="overlay_word('rase');" class="word word_points_1  word_guessed_wo26171 word_guessed_wo26107 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_rase" onmouseover="word_highlight('rase');" onmouseout="word_highlight_leave();">RASE</li> <li onclick="overlay_word('erahne');" class="word word_points_3 word_noguess" id="word_erahne">ERAHNE</li> <li onclick="overlay_word('ase');" class="word word_points_1 word_noguess" id="word_ase">ASE</li> <li onclick="overlay_word('hirse');" class="word word_points_2  word_guessed_gu1965" id="word_hirse" onmouseover="word_highlight('hirse');" onmouseout="word_highlight_leave();">HIRSE</li> <li onclick="overlay_word('airs');" class="word word_points_1 word_noguess" id="word_airs">AIRS</li> <li onclick="overlay_word('ria');" class="word word_points_1 word_noguess" id="word_ria">RIA</li> <li onclick="overlay_word('sinti');" class="word word_points_2 word_noguess" id="word_sinti">SINTI</li> <li onclick="overlay_word('arion');" class="word word_points_2 word_noguess" id="word_arion">ARION</li> <li onclick="overlay_word('erahnt');" class="word word_points_3 word_noguess" id="word_erahnt">ERAHNT</li> <li onclick="overlay_word('arni');" class="word word_points_1 word_noguess" id="word_arni">ARNI</li> <li onclick="overlay_word('ihrs');" class="word word_points_1 word_noguess" id="word_ihrs">IHRS</li> <li onclick="overlay_word('mire');" class="word word_points_1 word_noguess" id="word_mire">MIRE</li> <li onclick="overlay_word('rio');" class="word word_points_1 word_noguess" id="word_rio">RIO</li> <li onclick="overlay_word('sera');" class="word word_points_1 word_noguess" id="word_sera">SERA</li> <li onclick="overlay_word('bone');" class="word word_points_1  word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_bone" onmouseover="word_highlight('bone');" onmouseout="word_highlight_leave();">BONE</li> <li onclick="overlay_word('ihre');" class="word word_points_1  word_guessed_gu1965" id="word_ihre" onmouseover="word_highlight('ihre');" onmouseout="word_highlight_leave();">IHRE</li> <li onclick="overlay_word('moin');" class="word word_points_1 word_noguess" id="word_moin">MOIN</li> <li onclick="overlay_word('hirnis');" class="word word_points_3 word_noguess" id="word_hirnis">HIRNIS</li> <li onclick="overlay_word('ihres');" class="word word_points_2  word_guessed_gu1965" id="word_ihres" onmouseover="word_highlight('ihres');" onmouseout="word_highlight_leave();">IHRES</li> <li onclick="overlay_word('risa');" class="word word_points_1 word_noguess" id="word_risa">RISA</li> <li onclick="overlay_word('arms');" class="word word_points_1 word_noguess" id="word_arms">ARMS</li> <li onclick="overlay_word('boenhase');" class="word word_points_11 word_noguess" id="word_boenhase">BOENHASE</li> <li onclick="overlay_word('arnis');" class="word word_points_2 word_noguess" id="word_arnis">ARNIS</li> <li onclick="overlay_word('imin');" class="word word_points_1 word_noguess" id="word_imin">IMIN</li> <li onclick="overlay_word('boni');" class="word word_points_1  word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_boni" onmouseover="word_highlight('boni');" onmouseout="word_highlight_leave();">BONI</li> <li onclick="overlay_word('ion');" class="word word_points_1  word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_ion" onmouseover="word_highlight('ion');" onmouseout="word_highlight_leave();">ION</li> <li onclick="overlay_word('bont');" class="word word_points_1 word_noguess" id="word_bont">BONT</li> <li onclick="overlay_word('ares');" class="word word_points_1  word_guessed_gu1965" id="word_ares" onmouseover="word_highlight('ares');" onmouseout="word_highlight_leave();">ARES</li> <li onclick="overlay_word('ersah');" class="word word_points_2 word_noguess" id="word_ersah">ERSAH</li> <li onclick="overlay_word('mise');" class="word word_points_1 word_noguess" id="word_mise">MISE</li> <li onclick="overlay_word('beo');" class="word word_points_1  word_guessed_wo26171 word_guessed_wo26107 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_beo" onmouseover="word_highlight('beo');" onmouseout="word_highlight_leave();">BEO</li> <li onclick="overlay_word('omi');" class="word word_points_1  word_guessed_wo26171 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_omi" onmouseover="word_highlight('omi');" onmouseout="word_highlight_leave();">OMI</li> <li onclick="overlay_word('moira');" class="word word_points_2 word_noguess" id="word_moira">MOIRA</li> <li onclick="overlay_word('rahn');" class="word word_points_1 word_noguess" id="word_rahn">RAHN</li> <li onclick="overlay_word('saht');" class="word word_points_1 word_noguess" id="word_saht">SAHT</li> <li onclick="overlay_word('ihr');" class="word word_points_1  word_guessed_wo26107 word_guessed_gu1965 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_ihr" onmouseover="word_highlight('ihr');" onmouseout="word_highlight_leave();">IHR</li> <li onclick="overlay_word('niob');" class="word word_points_1 word_noguess" id="word_niob">NIOB</li> <li onclick="overlay_word('firnis');" class="word word_points_3 word_noguess" id="word_firnis">FIRNIS</li> <li onclick="overlay_word('arine');" class="word word_points_2 word_noguess" id="word_arine">ARINE</li> <li onclick="overlay_word('aes');" class="word word_points_1  word_guessed_wo26107" id="word_aes" onmouseover="word_highlight('aes');" onmouseout="word_highlight_leave();">AES</li> <li onclick="overlay_word('boe');" class="word word_points_1  word_guessed_wo26107 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_boe" onmouseover="word_highlight('boe');" onmouseout="word_highlight_leave();">BOE</li> <li onclick="overlay_word('oben');" class="word word_points_1  word_guessed_wo26107 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_oben" onmouseover="word_highlight('oben');" onmouseout="word_highlight_leave();">OBEN</li> <li onclick="overlay_word('ihn');" class="word word_points_1  word_guessed_wo26107 word_guessed_gu1965 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_ihn" onmouseover="word_highlight('ihn');" onmouseout="word_highlight_leave();">IHN</li> <li onclick="overlay_word('sire');" class="word word_points_1 word_noguess" id="word_sire">SIRE</li> <li onclick="overlay_word('ern');" class="word word_points_1  word_guessed_wo26107 word_guessed_gu1965" id="word_ern" onmouseover="word_highlight('ern');" onmouseout="word_highlight_leave();">ERN</li> <li onclick="overlay_word('arsin');" class="word word_points_2 word_noguess" id="word_arsin">ARSIN</li> <li onclick="overlay_word('bon');" class="word word_points_1  word_guessed_wo26107 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_bon" onmouseover="word_highlight('bon');" onmouseout="word_highlight_leave();">BON</li> <li onclick="overlay_word('sari');" class="word word_points_1  word_guessed_gu1965 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_sari" onmouseover="word_highlight('sari');" onmouseout="word_highlight_leave();">SARI</li> <li onclick="overlay_word('ras');" class="word word_points_1  word_guessed_wo26107 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_ras" onmouseover="word_highlight('ras');" onmouseout="word_highlight_leave();">RAS</li> <li onclick="overlay_word('iah');" class="word word_points_1 word_noguess" id="word_iah">IAH</li> <li onclick="overlay_word('iaht');" class="word word_points_1 word_noguess" id="word_iaht">IAHT</li> <li onclick="overlay_word('firne');" class="word word_points_2 word_noguess" id="word_firne">FIRNE</li> <li onclick="overlay_word('serin');" class="word word_points_2 word_noguess" id="word_serin">SERIN</li> <li onclick="overlay_word('serio');" class="word word_points_2 word_noguess" id="word_serio">SERIO</li> <li onclick="overlay_word('rahne');" class="word word_points_2 word_noguess" id="word_rahne">RAHNE</li> <li onclick="overlay_word('harms');" class="word word_points_2 word_noguess" id="word_harms">HARMS</li> <li onclick="overlay_word('mine');" class="word word_points_1  word_guessed_wo26171 word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_mine" onmouseover="word_highlight('mine');" onmouseout="word_highlight_leave();">MINE</li> <li onclick="overlay_word('ahnt');" class="word word_points_1  word_guessed_wo26107" id="word_ahnt" onmouseover="word_highlight('ahnt');" onmouseout="word_highlight_leave();">AHNT</li> <li onclick="overlay_word('mir');" class="word word_points_1  word_guessed_wo26171 word_guessed_gu1965" id="word_mir" onmouseover="word_highlight('mir');" onmouseout="word_highlight_leave();">MIR</li> <li onclick="overlay_word('hase');" class="word word_points_1 word_noguess" id="word_hase">HASE</li> <li onclick="overlay_word('mim');" class="word word_points_1 word_noguess" id="word_mim">MIM</li> <li onclick="overlay_word('ire');" class="word word_points_1 word_noguess" id="word_ire">IRE</li> <li onclick="overlay_word('nimm');" class="word word_points_1  word_guessed_wo26107" id="word_nimm" onmouseover="word_highlight('nimm');" onmouseout="word_highlight_leave();">NIMM</li> <li onclick="overlay_word('sahnt');" class="word word_points_2 word_noguess" id="word_sahnt">SAHNT</li> <li onclick="overlay_word('imine');" class="word word_points_2 word_noguess" id="word_imine">IMINE</li> <li onclick="overlay_word('harne');" class="word word_points_2  word_guessed_wo26107" id="word_harne" onmouseover="word_highlight('harne');" onmouseout="word_highlight_leave();">HARNE</li> <li onclick="overlay_word('ahne');" class="word word_points_1  word_guessed_wo26107 word_guessed_gu1965" id="word_ahne" onmouseover="word_highlight('ahne');" onmouseout="word_highlight_leave();">AHNE</li> <li onclick="overlay_word('arin');" class="word word_points_1 word_noguess" id="word_arin">ARIN</li> <li onclick="overlay_word('noir');" class="word word_points_1 word_noguess" id="word_noir">NOIR</li> <li onclick="overlay_word('omis');" class="word word_points_1 word_noguess" id="word_omis">OMIS</li> <li onclick="overlay_word('harnt');" class="word word_points_2 word_noguess" id="word_harnt">HARNT</li> <li onclick="overlay_word('benimm');" class="word word_points_3 word_noguess" id="word_benimm">BENIMM</li> <li onclick="overlay_word('aser');" class="word word_points_1 word_noguess" id="word_aser">ASER</li> <li onclick="overlay_word('mint');" class="word word_points_1  word_guessed_wo10063 word_guessed_team_alfredbrenz" id="word_mint" onmouseover="word_highlight('mint');" onmouseout="word_highlight_leave();">MINT</li>

      </ul>


    </div>

  </div>  

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>	
	<script type="text/javascript" src="/_ah/channel/jsapi"></script>
  	<script src="js/chat.js"></script>
  	<script src="js/user.js"></script>
  	<script src="js/main.js"></script>
  	
  	<script>
		$(function() {
			wortopia = new Wortopia({
				user: <%= user.asJSON() %>,
				channelToken: '<%= channelToken.getToken() %>',
			});
		});
	</script>
</body>
</html>