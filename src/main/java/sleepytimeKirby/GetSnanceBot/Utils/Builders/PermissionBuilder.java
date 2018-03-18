package sleepytimeKirby.GetSnanceBot.Utils.Builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.core.Permission;

public class PermissionBuilder {
//Constructor will be booleans
//Setter Methods to set booleans
//Get Method will be List<Permissions>
	
	private HashMap <String,Boolean> permissionsToInclude = new HashMap <String,Boolean>();
	private HashMap <String,Permission> permissions = new HashMap <String,Permission>();
	public PermissionBuilder() {
		permissionsToInclude.put("createInstantInvite", false);
		permissionsToInclude.put("kickMembers", false);
		permissionsToInclude.put("banMembers", false);
		permissionsToInclude.put("administrator", false);
		permissionsToInclude.put("manageChannel", false);
		permissionsToInclude.put("manageServer", false);
		permissionsToInclude.put("messageAddRecation", false);
		permissionsToInclude.put("viewAuditLogs", false);
		permissionsToInclude.put("viewChannel", false);
		permissionsToInclude.put("messageRead", false);
		permissionsToInclude.put("messageWrite", false);
		permissionsToInclude.put("messageTTS", false);
		permissionsToInclude.put("messageManage", false);
		permissionsToInclude.put("messageEmbedLink", false);
		permissionsToInclude.put("messageAttachFiles", false);
		permissionsToInclude.put("messageHistory", false);
		permissionsToInclude.put("messageMentionEveryone", false);
		permissionsToInclude.put("messageExtEmoji", false);
		permissionsToInclude.put("voiceConnect", false);
		permissionsToInclude.put("voiceSpeak", false);
		permissionsToInclude.put("voiceMuteOthers", false);
		permissionsToInclude.put("voiceDeafOthers", false);
		permissionsToInclude.put("voiceMoveOthers", false);
		permissionsToInclude.put("voiceUseAutoDetection", false);
		permissionsToInclude.put("nicknameChange", false);
		permissionsToInclude.put("nicknameManage", false);
		permissionsToInclude.put("manageRoles", false);
		permissionsToInclude.put("managePermissions", false);
		permissionsToInclude.put("manageWebhooks", false);
		permissionsToInclude.put("manageEmotes", false);
		permissions.put("createInstantInvite", Permission.CREATE_INSTANT_INVITE);
		permissions.put("kickMembers", Permission.KICK_MEMBERS);
		permissions.put("banMembers", Permission.BAN_MEMBERS);
		permissions.put("administrator", Permission.ADMINISTRATOR);
		permissions.put("manageChannel", Permission.MANAGE_CHANNEL);
		permissions.put("manageServer", Permission.MANAGE_SERVER);
		permissions.put("messageAddRecation", Permission.MESSAGE_ADD_REACTION);
		permissions.put("viewAuditLogs", Permission.VIEW_AUDIT_LOGS);
		permissions.put("viewChannel", Permission.VIEW_CHANNEL);
		permissions.put("messageRead", Permission.MESSAGE_READ);
		permissions.put("messageWrite", Permission.MESSAGE_WRITE);
		permissions.put("messageTTS", Permission.MESSAGE_TTS);
		permissions.put("messageManage", Permission.MESSAGE_MANAGE);
		permissions.put("messageEmbedLink", Permission.MESSAGE_EMBED_LINKS);
		permissions.put("messageAttachFiles", Permission.MESSAGE_ATTACH_FILES);
		permissions.put("messageHistory", Permission.MESSAGE_HISTORY);
		permissions.put("messageMentionEveryone", Permission.MESSAGE_MENTION_EVERYONE);
		permissions.put("messageExtEmoji", Permission.MESSAGE_EXT_EMOJI);
		permissions.put("voiceConnect", Permission.VOICE_CONNECT);
		permissions.put("voiceSpeak", Permission.VOICE_SPEAK);
		permissions.put("voiceMuteOthers", Permission.VOICE_MUTE_OTHERS);
		permissions.put("voiceDeafOthers", Permission.VOICE_DEAF_OTHERS);
		permissions.put("voiceMoveOthers", Permission.VOICE_MOVE_OTHERS);
		permissions.put("voiceUseAutoDetection", Permission.VOICE_USE_VAD);
		permissions.put("nicknameChange", Permission.NICKNAME_CHANGE);
		permissions.put("nicknameManage", Permission.NICKNAME_MANAGE);
		permissions.put("manageRoles", Permission.MANAGE_ROLES);
		permissions.put("managePermissions", Permission.MANAGE_PERMISSIONS);
		permissions.put("manageWebhooks", Permission.MANAGE_WEBHOOKS);
		permissions.put("manageEmotes", Permission.MANAGE_EMOTES);
		
	} 
	public void setPermissions (boolean createInstantInvite, boolean kickMembers, boolean banMembers,
			boolean administrator, boolean manageChannel, boolean manageServer, boolean messageAddRecation,
			boolean viewAuditLogs, boolean viewChannel, boolean messageRead, boolean messageWrite, boolean messageTTS,
			boolean messageManage, boolean messageEmbedLink, boolean messageAttachFiles, boolean messageHistory,
			boolean messageMentionEveryone, boolean messageExtEmoji, boolean voiceConnect, boolean voiceSpeak,
			boolean voiceMuteOthers, boolean voiceDeafOthers, boolean voiceMoveOthers, boolean voiceUseAutoDetection,
			boolean nicknameChange, boolean nicknameManage, boolean manageRoles, boolean managePermissions,
			boolean manageWebhooks, boolean manageEmotes) {
		permissionsToInclude.put("createInstantInvite", createInstantInvite);
		permissionsToInclude.put("kickMembers", kickMembers);
		permissionsToInclude.put("banMembers", banMembers);
		permissionsToInclude.put("administrator", administrator);
		permissionsToInclude.put("manageChannel", manageChannel);
		permissionsToInclude.put("manageServer", manageServer);
		permissionsToInclude.put("messageAddRecation", messageAddRecation);
		permissionsToInclude.put("viewAuditLogs", viewAuditLogs);
		permissionsToInclude.put("viewChannel", viewChannel);
		permissionsToInclude.put("messageRead", messageRead);
		permissionsToInclude.put("messageWrite", messageWrite);
		permissionsToInclude.put("messageTTS", messageTTS);
		permissionsToInclude.put("messageManage", messageManage);
		permissionsToInclude.put("messageEmbedLink", messageEmbedLink);
		permissionsToInclude.put("messageAttachFiles", messageAttachFiles);
		permissionsToInclude.put("messageHistory", messageHistory);
		permissionsToInclude.put("messageMentionEveryone", messageMentionEveryone);
		permissionsToInclude.put("messageExtEmoji", messageExtEmoji);
		permissionsToInclude.put("voiceConnect", voiceConnect);
		permissionsToInclude.put("voiceSpeak", voiceSpeak);
		permissionsToInclude.put("voiceMuteOthers", voiceMuteOthers);
		permissionsToInclude.put("voiceDeafOthers", voiceDeafOthers);
		permissionsToInclude.put("voiceMoveOthers", voiceMoveOthers);
		permissionsToInclude.put("voiceUseAutoDetection", voiceUseAutoDetection);
		permissionsToInclude.put("nicknameChange", nicknameChange);
		permissionsToInclude.put("nicknameManage", nicknameManage);
		permissionsToInclude.put("manageRoles", manageRoles);
		permissionsToInclude.put("managePermissions", managePermissions);
		permissionsToInclude.put("manageWebhooks", manageWebhooks);
		permissionsToInclude.put("manageEmotes", manageEmotes);
	} 
	
	public List<Permission> getAllowed(){
		List<Permission> allowed = new ArrayList<Permission>();
		for(String key:permissionsToInclude.keySet()) {
			if(permissionsToInclude.get(key)) {
				allowed.add(permissions.get(key));
			}
		}
		return allowed;
	}
	public List<Permission> getDenied(){
		List<Permission> denied = new ArrayList<Permission>();
		for(String key:permissionsToInclude.keySet()) {
			if(!permissionsToInclude.get(key)) {
				denied.add(permissions.get(key));
			}
		}
		return denied;
	}
	
}
