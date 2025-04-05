# FEATURE BOARD

## Single-Chat
Have a setting where the conversations are rapid fire and unlimited (generated by ai, generates the sentence and the voice)
- To continue the conversation, users must have a score >= 80, score based off each response (time limit etc)
- Two settings:
  
    a) Type chat (based off my webpage), A.I. generates text messages, user writes back text messages (point system)
  
    b) Speak chat, A.I. generates vocal messages, user speaks back (point system)

## Multi-Chat
Have a setting where people can communicate globally with other countries in the rooms (kinda like COC global chat, but with voice)
- Number of people per room: 2, 4, 8
- Three modes (Mode 1 and 2 essentially just Microsoft Teams)
  
    a) Mode 1: Casual speak (randomize joiners have limits), no point system
  
    b) Mode 2: Custom speak (invite only, based off limits), no point system
  
    c) Mode 3: Competitive rounds, generate random topic, have users speak for x minutes, have AI take input, analyze speech, assign points. 
- Gamified version, not actually conversational, set number of people assigned to a room, game consists of y rounds for x*n total minutes, where each user speaks for x minutes solo. 
- At the end, points and winner is announced. Points contribute to global leader board.

## Storage and Display
Persistently store info (i.e., username (public), passcode (private), points (public), proficiency level (public), region (public))
- Display this handle (username, points, proficiency level, region):
  
  1. For Single-Chat, display points for singleChatPoints
     
  2. For Multi-Chat, display points for multiChatPoints

## Leaderboard Pages
Two different leaderboard pages:

  1. For Single-Chat, displays handle where points = singleChatPoints
     
  2. For Multi-Chat, displays handle where points = multiChatPoints 
